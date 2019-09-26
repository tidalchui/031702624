import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.alibaba.fastjson.JSONArray;


public class Main {
	public static void main(String[] args) {
		try{
			StringBuffer sb = new StringBuffer("");
			FileInputStream fis = new FileInputStream(args[0]);
			InputStreamReader read = new InputStreamReader(fis, "utf-8");
			BufferedReader br = new BufferedReader(read);
			String addr = null;
			JSONArray jArray = new JSONArray();
			
			while ((addr = br.readLine()) != null){
				String level = null, name = null, num = null;
				String[] temps = addr.split("!", 2);
				level = temps[0];
				addr = temps[1];
				Pattern pname = Pattern.compile("^.+,");
				Matcher mname = pname.matcher(addr);
				if(mname.find())
				{
					name = mname.group(0);
					addr = addr.replaceAll(name, "");
					name = name.replaceAll(",","");
				}
				Pattern pnum = Pattern.compile("\\d{11}");
				Matcher mnum = pnum.matcher(addr);
				if(mnum.find())
				{
					num = mnum.group(0);
					addr = addr.replaceAll(num, "");
				}
				addr = addr.replaceAll(" ", "");
				addr = addr.replaceAll("\\.", "");
				
				ArrayList<String> arrayList = getMatchesAddress(level, addr);
				Map<String, Object> JsonMap = getJson(arrayList, name, num);
				jArray.add(JsonMap);
			}
			sb.append(jArray.toJSONString()).append("\n");
			br.close();
			read.close();
			FileWriter writer = new FileWriter(args[1]);
			BufferedWriter bw = new BufferedWriter(writer);
			bw.write(sb.toString());
			bw.close();
			writer.close();
			}
			
			catch (FileNotFoundException e){
				e.printStackTrace();
			} catch (IOException e){
				e.printStackTrace();
			}
			}
	
public static ArrayList<String> getMatchesAddress(String level, String addr) {
	String paddr="(?<province>[^ʡ]+������|.*?ʡ|.*?������)?(?<city>[^��]+������|.*?����|.*?������λ|.+��|��Ͻ��|.*?��|.*?��)?(?<county>[^��]+��|.{0,4}��|.+��|.+��|.+����|.+��)?(?<town>.*?��|.*?�ֵ�|.+��)?(?<detail>.*)";
    Matcher maddr = Pattern.compile(paddr).matcher(addr);
    String province = "", city = "", county = "", town = "", detail = "";
    if(maddr.find())
    {
    	province = maddr.group(1) == null?"":maddr.group(1);
    	city = maddr.group(2) == null?"":maddr.group(2);
    	county = maddr.group(3) == null?"":maddr.group(3);
    	town = maddr.group(4) == null?"":maddr.group(4);
    	detail = maddr.group(5) == null?"":maddr.group(5);
    }
    
    
    ArrayList<String> arrayList = new ArrayList<String>();
	arrayList.add(province);
	arrayList.add(city);
	arrayList.add(county);
	arrayList.add(town);
    if(level.equals("1"))
    {
    	arrayList.add(detail);
    }
    else {
    	String paddr2="(?<road>.*?·|.*?��|)?(?<mark>.*?��)?(?<detail2>.*)";
        Matcher maddr2 = Pattern.compile(paddr2).matcher(detail);
        String road = "", mark = "", detail2 = "";
        if(maddr2.find())
        {
        	road = maddr2.group(1) == null?"":maddr2.group(1);
        	mark = maddr2.group(2) == null?"":maddr2.group(2);
        	detail2 = maddr2.group(3) == null?"":maddr2.group(3);
        }
		arrayList.add(road);
		arrayList.add(mark);
		arrayList.add(detail2);
	}
	return arrayList;
}
static Map<String, Object> map = null;

public static Map<String, Object> getJson(ArrayList<String> arrayList, String name, String num) {
	Object[] array =  arrayList.toArray();
	map = new LinkedHashMap<String,Object>();
	map.put("����", name.toString());
	map.put("�ֻ�", num.toString());
	map.put("��ַ", array);
	return map;
	}
}