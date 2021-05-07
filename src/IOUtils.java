import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class IOUtils {
	public static void writeString(String path, String content) throws IOException {
		Files.write(Paths.get(path), content.getBytes(StandardCharsets.UTF_8));
	}
	public static String readString(String path) throws IOException {
		return new String(Files.readAllBytes(Paths.get(path)), StandardCharsets.UTF_8);
	}
	public static void writeClipboard(String content) {
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(content), null);
	}
	public static String readClipboard() {
		try {
			return (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
		} catch (HeadlessException | UnsupportedFlavorException | IOException e) {
			e.printStackTrace();
		}
		return "";
	}
}
