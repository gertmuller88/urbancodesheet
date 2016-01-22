package com.pitang.control;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.codehaus.jettison.json.JSONException;
import com.pitang.business.Component;
import com.pitang.business.ComponentTemplate;
import com.pitang.business.Environment;
import com.pitang.business.MetaProperty;
import com.pitang.business.Property;
import com.pitang.business.PropertyScope;
import com.pitang.business.ResourcePool;
import com.pitang.gui.MainScreen;
import com.pitang.gui.WaitScreen;
import com.pitang.util.Session;
import com.pitang.util.Util;

public abstract class Controller {
	
	public static void init() throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
		final Properties properties = new Properties();

		try(InputStream input = new FileInputStream(Util.CONFIG_FILE)) {
			properties.load(input);

			final Session session = Session.getInstance();
			session.setAttribute(Util.URL, properties.getProperty(Util.URL));
			session.setAttribute(Util.USERNAME, properties.getProperty(Util.USERNAME));
			session.setAttribute(Util.PASSWORD, properties.getProperty(Util.PASSWORD));
			session.setAttribute(Util.AUTH_TOKEN, properties.getProperty(Util.AUTH_TOKEN));
		} catch(final IOException e) {
			e.printStackTrace();
		}

		Logger.getRootLogger().setLevel(org.apache.log4j.Level.OFF);
		DOMConfigurator.configure(Util.LOG4J_CONFIG_FILE);
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

		new MainScreen().setVisible(true);
	}
	
	public String execute(final WaitScreen dialog, final File file, final Integer start, final Integer end) throws InvalidFormatException, IOException, IllegalStateException, URISyntaxException, JSONException {
		try(final Workbook workbook = WorkbookFactory.create(file)) {
			final Map<String, ComponentTemplate> templates = Controller.loadTemplates(workbook);
			final Map<String, Component> components = Controller.loadComponents(workbook, templates, start, end);
			
			dialog.setMaximum(components.size());
			
			for(final Entry<String, Component> component : components.entrySet()) {
				this.iteration(component.getValue());
				dialog.incrementProgressValue();
			}
			
			return this.getMessage();
		}
	}
	
	protected abstract void iteration(final Component component) throws InvalidFormatException, IOException, IllegalStateException, URISyntaxException, JSONException;

	protected abstract String getMessage();
	
	protected static Map<String, ComponentTemplate> loadTemplates(final Workbook workbook) {
		final Sheet sheet = workbook.getSheet("Templates");
		final Map<String, ComponentTemplate> templates = new LinkedHashMap<>();

		for(int i = 1; i < sheet.getLastRowNum(); i++ ) {
			final Row row = sheet.getRow(i);

			if(row != null) {
				final String templateName = ((row.getCell(0) != null) && (row.getCell(0).getStringCellValue() != null)) ? row.getCell(0).getStringCellValue().trim() : null;
				
				if((templateName != null) && !templateName.isEmpty()) {
					ComponentTemplate template = templates.get(templateName);
					
					if(template == null) {
						template = new ComponentTemplate(templateName);
						templates.put(templateName, template);
					}
				}
			}
		}

		Controller.loadTemplatesProperties(workbook, templates);
		Controller.loadPools(workbook, templates);
		
		return templates;
	}
	
	private static void loadTemplatesProperties(final Workbook workbook, final Map<String, ComponentTemplate> templates) {
		final Sheet sheet = workbook.getSheet("Templates-Props");

		for(int i = 1; i < sheet.getLastRowNum(); i++ ) {
			final Row row = sheet.getRow(i);

			if(row != null) {
				final String templateName = ((row.getCell(0) != null) && (row.getCell(0).getStringCellValue() != null)) ? row.getCell(0).getStringCellValue().trim() : null;

				if((templateName != null) && !templateName.isEmpty()) {
					final ComponentTemplate template = templates.get(templateName);

					if(template != null) {
						final String propertyName = ((row.getCell(1) != null) && (row.getCell(1).getStringCellValue() != null)) ? row.getCell(1).getStringCellValue().trim() : null;

						if((propertyName != null) && !propertyName.isEmpty()) {
							MetaProperty metaProperty = template.getProperties().get(propertyName);

							if(metaProperty == null) {
								metaProperty = new MetaProperty(propertyName);
								template.getProperties().put(propertyName, metaProperty);
							}

							final String propertyScope = ((row.getCell(2) != null) && (row.getCell(2).getStringCellValue() != null)) ? row.getCell(2).getStringCellValue().trim() : null;
							metaProperty.setScope(PropertyScope.fromName(propertyScope));
						}
					}
				}
			}
		}
	}
	
	private static void loadPools(final Workbook workbook, final Map<String, ComponentTemplate> templates) {
		final Sheet sheet = workbook.getSheet("Pools");

		for(int i = 1; i < sheet.getLastRowNum(); i++ ) {
			final Row row = sheet.getRow(i);

			if(row != null) {
				final String templateName = ((row.getCell(0) != null) && (row.getCell(0).getStringCellValue() != null)) ? row.getCell(0).getStringCellValue().trim() : null;

				if((templateName != null) && !templateName.isEmpty()) {
					final ComponentTemplate template = templates.get(templateName);

					if(template != null) {
						final String poolName = ((row.getCell(1) != null) && (row.getCell(1).getStringCellValue() != null)) ? row.getCell(1).getStringCellValue().trim() : null;

						if((poolName != null) && !poolName.isEmpty()) {
							ResourcePool pool = template.getPools().get(poolName);

							if(pool == null) {
								pool = new ResourcePool(poolName);
								template.getPools().put(poolName, pool);
							}
							
							final String environmentName = ((row.getCell(2) != null) && (row.getCell(2).getStringCellValue() != null)) ? row.getCell(2).getStringCellValue().trim() : null;
							
							if((environmentName != null) && !environmentName.isEmpty()) {
								Environment environment = pool.getEnvironments().get(poolName);
								
								if(environment == null) {
									environment = new Environment(environmentName, pool);
									pool.getEnvironments().put(environmentName, environment);
								}
								
								final String propertyName = ((row.getCell(3) != null) && (row.getCell(3).getStringCellValue() != null)) ? row.getCell(3).getStringCellValue().trim() : null;
								final String propertyValue = ((row.getCell(4) != null) && (row.getCell(4).getStringCellValue() != null)) ? row.getCell(4).getStringCellValue().trim() : null;
								
								if((propertyName != null) && !propertyName.isEmpty() && (propertyValue != null) && !propertyValue.isEmpty()) {
									final MetaProperty metaProperty = template.getPropertiesByScope(PropertyScope.ENVIRONMENT).get(propertyName);
									
									if(metaProperty != null) {
										final Property property = new Property(metaProperty, propertyValue);
										environment.getProperties().put(propertyName, property);
									}
								}
							}
						}
					}
				}
			}
		}
	}

	protected static Map<String, Component> loadComponents(final Workbook workbook, final Map<String, ComponentTemplate> templates, final Integer start, final Integer end) {
		final Sheet sheet = workbook.getSheet("Componentes");
		final Map<String, Component> components = new HashMap<>();

		for(int i = (start >= 2 ? start - 1 : 1); i <= (end > sheet.getLastRowNum() ? sheet.getLastRowNum() : end); i++ ) {
			final Row row = sheet.getRow(i);

			if(row != null) {
				final String componentName = ((row.getCell(0) != null) && (row.getCell(0).getStringCellValue() != null)) ? row.getCell(0).getStringCellValue().trim() : null;
				
				if((componentName != null) && !componentName.isEmpty()) {
					Component component = components.get(componentName);
					
					if(component == null) {
						component = new Component(componentName);
						components.put(componentName, component);
					}

					final String application = ((row.getCell(1) != null) && (row.getCell(1).getStringCellValue() != null)) ? row.getCell(1).getStringCellValue().trim() : null;
					component.setApplication(application);

					final String acronym = ((row.getCell(2) != null) && (row.getCell(2).getStringCellValue() != null)) ? row.getCell(2).getStringCellValue().trim() : null;
					component.setAcronym(acronym);

					final String templateName = ((row.getCell(3) != null) && (row.getCell(3).getStringCellValue() != null)) ? row.getCell(3).getStringCellValue().trim() : null;

					if((templateName != null) && !templateName.isEmpty()) {
						final ComponentTemplate template = templates.get(templateName);

						if(template != null) {
							component.setTemplate(template);
							final String poolName = ((row.getCell(4) != null) && (row.getCell(4).getStringCellValue() != null)) ? row.getCell(4).getStringCellValue().trim() : null;

							if((poolName != null) && !poolName.isEmpty()) {
								final ResourcePool pool = template.getPools().get(poolName);

								if(pool != null) {
									component.setPool(pool);
								}
							}
						}
					}
				}
			}
		}
		
		Controller.loadComponentsProperties(workbook, components);

		return components;
	}
	
	private static void loadComponentsProperties(final Workbook workbook, final Map<String, Component> components) {
		final Sheet sheet = workbook.getSheet("Componentes-Props");

		for(int i = 1; i < sheet.getLastRowNum(); i++ ) {
			final Row row = sheet.getRow(i);

			if(row != null) {
				final String componentName = ((row.getCell(0) != null) && (row.getCell(0).getStringCellValue() != null)) ? row.getCell(0).getStringCellValue().trim() : null;

				if((componentName != null) && !componentName.isEmpty()) {
					final Component component = components.get(componentName);

					if(component != null) {
						final String propertyName = ((row.getCell(1) != null) && (row.getCell(1).getStringCellValue() != null)) ? row.getCell(1).getStringCellValue().trim() : null;
						final String propertyValue = ((row.getCell(2) != null) && (row.getCell(2).getStringCellValue() != null)) ? row.getCell(2).getStringCellValue().trim() : null;

						if((propertyName != null) && !propertyName.isEmpty() && (propertyValue != null) && !propertyValue.isEmpty()) {
							final MetaProperty metaProperty = component.getTemplate().getPropertiesByScope(PropertyScope.COMPONENT).get(propertyName);

							if(metaProperty != null) {
								final Property property = new Property(metaProperty, propertyValue);
								component.getProperties().put(propertyName, property);
							}
						}
					}
				}
			}
		}
	}

}
