import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import javax.imageio.ImageIO;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import waffle.util.Base64;

public class ImgurUpload {

	public static void main(String[] args) throws IOException, ParseException {
		// TODO Auto-generated method stub
		String IMGUR_POST_URI = "https://api.imgur.com/3/upload";
		String IMGUR_API_KEY = "API_KEY_HERE";
		BufferedImage image = null;
		String localImage = System.getProperty("user.home")+"/avatar.jpg";
		File file = new File(localImage);
		image = ImageIO.read(file);
		ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
		String extension = "png";
		if(file.getName().lastIndexOf(".") != -1 && file.getName().lastIndexOf(".") != 0){
			extension = file.getName().substring(file.getName().lastIndexOf(".")+1);
		}
		ImageIO.write(image, extension, byteArray);
		byte[] byteImage = byteArray.toByteArray();
		String dataImage = Base64.encode(byteImage);
		String data = URLEncoder.encode("image", "UTF-8") + "=" +URLEncoder.encode(dataImage, "UTF-8");
		URL url = new URL(IMGUR_POST_URI);
		
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setDoOutput(true);
		conn.setDoInput(true);
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Authorization", "Client-ID "+IMGUR_API_KEY);
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		
		conn.connect();
		
		OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
		wr.write(data);
		wr.flush();
		
		BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		String line = rd.readLine();
		JSONObject dataObject = new JSONObject();
		while(line != null){
			JSONObject jsonObject = (JSONObject) new JSONParser().parse(line);
			 dataObject = (JSONObject) jsonObject.get("data");
			line = rd.readLine();
			
			
		}

		wr.close();
		System.out.println(dataObject.get("link").toString());
	}

}
