import jp.digitalmuseum.connector.Connector;
import jp.digitalmuseum.connector.FantomConnector;

public class FantomDiscovery {
	public static final byte DIRECT_COMMAND_REPLY = (byte)0x00;
	public static final byte SYSTEM_COMMAND_REPLY = (byte)0x01;
	public static final byte DIRECT_COMMAND_NOREPLY = (byte)0x80;
	public static final byte GET_FIRMWARE_VERSION = (byte)0x88;
	public static final byte GET_BATTERY_LEVEL = 0x0B;

	public static void main(String[] args) {
		for (String connectionString : FantomConnector.queryIdentifiers()) {
			System.out.print("connecting to ");
			System.out.println(connectionString);
			FantomConnector connector = new FantomConnector(connectionString);
			getFirmware(connector, 7);
			getFirmware(connector, 3);
			getFirmware(connector, 10);
		}
		// BluetoothConnector connector = new BluetoothConnector("btspp://001653055D42");
		// getFirmware(connector);
	}

	public static void getFirmware(Connector connector, int len) {
		connector.connect();
		System.out.print("sending command: ");
		if (!connector.write(new byte[] {
				SYSTEM_COMMAND_REPLY,
				GET_FIRMWARE_VERSION
		})) {
			System.out.println("failed");
		} else {
			System.out.println("succeeded");
		}
		System.out.print("waiting for reply: ");
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		byte[] data = new byte[len];
		int read;
		if ((read = connector.read(data)) < 0) {
			System.out.println("failed");
		} else {
			System.out.println(read);
			System.out.print("succeeded,");
			for (int i = 0; i < data.length; i ++) {
				System.out.print(" ");
				System.out.print(data[i]);
			}
			System.out.println();
		}
		while ((read = connector.read()) > 0)
			System.out.println(read);
		connector.disconnect();
	}
}
