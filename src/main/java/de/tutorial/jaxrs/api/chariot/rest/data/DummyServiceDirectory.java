package de.tutorial.jaxrs.api.chariot.rest.data;
import java.util.List;
import java.util.UUID;

import de.tutorial.jaxrs.model.runtimeenvironment.Device;
import de.tutorial.jaxrs.model.runtimeenvironment.RuntimeEnvironment;
import de.tutorial.jaxrs.model.runtimeenvironment.SensingDevice;
import de.tutorial.jaxrs.model.util.builder.DeviceBuilder;
import de.tutorial.jaxrs.model.util.builder.PropertyBuilder;

import java.net.URI;
import java.util.ArrayList;

/**
 * This will be replaced by a connector to DS using whatever protocols.
 * @author dang
 *
 */
public class DummyServiceDirectory {
	static DummyServiceDirectory DEFAULT_DS = null;

	private static List<RuntimeEnvironment> runtimeEnvs = new ArrayList<RuntimeEnvironment>();
	private static List<Device> devices = new ArrayList<Device>();

	static {
		runtimeEnvs.add(createRe(UUID.randomUUID(), "IoLite", "active"));
		
		String ioliteNamespace = "http://iolite.de#";
		URI ioliteUri = URI.create("http://iolite.de");

		/* Sensing devices */
		devices.add(new DeviceBuilder()
				.setName("HeartRateMonitor")
				.setNamespaceUri(ioliteUri)
				.setIdentifier(URI.create(ioliteNamespace + "HeartRateMonitor"))
				.addMandatoryProperties(new PropertyBuilder()
						.setLabel("heartRate")
						.setUnit("hpm")
						.setValue(Integer.parseInt("80"))
						.build())
				.addOptionalProperties(new PropertyBuilder()
						.setLabel("batteryLevel")
						.setUnit("%")
						.setValue(Integer.parseInt("80"))
						.build())
				.buildSensingDevice());

		/* Actualting devices */
		devices.add(new DeviceBuilder()
				.setName("WashingMachine")
				.setNamespaceUri(ioliteUri)
				.setIdentifier(URI.create(ioliteNamespace + "WashingMachine"))
				.addOptionalProperties(new PropertyBuilder()
						.setLabel("waterTemperatureSetting")
						.setUnit("Celsius")
						.setValue(Integer.parseInt("30"))
						.build())
				.buildSensingDevice());

	}

	//private DummyServiceDirectory()
	//{  }

	// static method to create instance of Singleton class
	public static DummyServiceDirectory createServiceDirectoryConnector() {
		if (DEFAULT_DS == null)
			DEFAULT_DS = new DummyServiceDirectory();

		return DEFAULT_DS;
	}

	public RuntimeEnvironment findReById(UUID id) {
		for (RuntimeEnvironment re : runtimeEnvs) {
			if (re.getId().equals(id)) {
				return re;
			}
		}
		return null;
	}

	public void addRe(RuntimeEnvironment re) {
		if(re.getId() == null)
			return;
		if (runtimeEnvs.size() > 0) {
			for (int i = runtimeEnvs.size() - 1; i >= 0; i--) {
				if (runtimeEnvs.get(i).getId().equals(re.getId())) {
					runtimeEnvs.remove(i);
				}
			}
		}
		runtimeEnvs.add(re);
	}

	public boolean removeRe(UUID id) {
		if (runtimeEnvs.size() > 0) {
			for (int i = runtimeEnvs.size() - 1; i >= 0; i--) {
				if (runtimeEnvs.get(i).getId().equals(id)) {
					runtimeEnvs.remove(i);
					return true;
				}
			}
		}
		return false;
	}

	private static RuntimeEnvironment createRe(UUID id, String platform, String string) {
		RuntimeEnvironment re = new RuntimeEnvironment();
		re.setId(id);
		return re;
	}

	public boolean removeDevice(URI id) {
		for (Device device : this.devices) {
			if (device.getIdentifier().equals(id)) {
				devices.remove(device);
			}
			return true;
		}
		return false;
	}

	public void addDevice(Device device) {
		if(device.getIdentifier() == null)
			return;
		if (devices.size() > 0) {
			for (int i = devices.size() - 1; i >= 0; i--) {
				if (devices.get(i).getIdentifier().equals(device.getIdentifier())) {
					devices.remove(i);
				}
			}
		}
		devices.add(device);

	}
	
}