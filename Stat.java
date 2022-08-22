import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;
import java.io.*;
import java.util.*;
import com.google.gson.Gson;

public class Stat {

	public static void main(String[] args) throws Exception {
		try (Stream<Path> paths = Files.walk(Paths.get("."))) {
			paths.filter(Files::isRegularFile)
				.forEach(Stat::scanFileWrap);
		}
	}

	static int cnt = 0;
	static double sumPrc = 0.0;

	static void scanFileWrap(Path path) {
		try {
			scanFile(path);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	static void scanFile(Path path) throws Exception {

		if(!path.toString().endsWith("log")) return;

		Gson gson = new Gson();
		LineNumberReader in = new LineNumberReader(new FileReader(path.toFile()));
		String s = "";
		while((s = in.readLine()) != null) {
			s = s.substring(s.indexOf("logPrc =") + 9);
			HashMap map = gson.fromJson(s, HashMap.class);
			System.out.println("map = " + map);
			System.out.println("rsltPrc = " + map.get("rsltPrc"));
			cnt++;
			double prc = 0.0;
			Object objPrc = map.get("rsltPrc");
			if(objPrc instanceof Double) {
				prc = (Double)objPrc;
			} else {
				prc = Double.parseDouble((String)objPrc);
			}
			if(prc <= 400000000.0) {
				sumPrc += prc;
			}
		}
		System.out.println("cnt = " + cnt);
		System.out.println("sumPrc = " + sumPrc);
	}
}
