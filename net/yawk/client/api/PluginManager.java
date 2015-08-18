package net.yawk.client.api;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import net.yawk.client.Client;
import net.yawk.client.gui.AbstractComponent;
import net.yawk.client.gui.Window;
import net.yawk.client.gui.components.buttons.ModButton;
import net.yawk.client.modmanager.Mod;
import net.yawk.client.modmanager.PluginMod;
import net.yawk.client.utils.ClientUtils;
import net.yawk.client.utils.FileUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.darkmagician6.eventapi.EventManager;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
/**
 * Handles all plugin data and plugin loading
 * @author 10askinsw
 */
public class PluginManager {

	private File plugins = new File(Client.getFullDir(), "plugins");

	public List<PluginData> pluginData = new ArrayList<PluginData>();
	private Queue<PluginData> downloadQueue = new ConcurrentLinkedQueue<PluginData>();
	public Map<PluginData,Window> pluginWindows = new HashMap<PluginData,Window>();

	private Thread downloadThread;

	/**
	 * Loads the plugins in the /plugins/ folder at startup
	 * @throws ClassNotFoundException
	 * @throws MalformedURLException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	public void load() throws ClassNotFoundException, MalformedURLException, InstantiationException, IllegalAccessException{

		//Create the plugins directory
		if(!plugins.exists()){
			plugins.mkdir();
		}

		//Get plugins from the website
		try {

			Document doc = Jsoup.connect("http://www.yawk.net/mods/list.php").userAgent(ClientUtils.USER_AGENT).get();

			JsonArray arr = new JsonParser().parse(doc.text()).getAsJsonArray();

			for(JsonElement el : arr){

				JsonObject json = el.getAsJsonObject();
				pluginData.add(new PluginData(json.get("name").getAsString(), json.get("description").getAsString(), json.get("file").getAsString(), json.get("filename").getAsString(), json.get("version").getAsInt(), false, false));
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		//Downloads any plugins we don't have downloaded yet
		for(PluginData available : pluginData){

			File pluginFile = new File(plugins, available.getFileName());

			if(!pluginFile.exists()){
				this.downloadPlugin(available);
			}
		}
	}

	/**
	 * Finds the Plugin object with a particular name 
	 * @param name
	 * @return the data of the plugin with that name
	 */
	public PluginData getPluginDataByName(String name){

		for(PluginData data : pluginData){
			if(data.getName().equals(name)){
				return data;
			}
		}

		return null;
	}

	/**
	 * Finds the Plugin object which is for a certiain file
	 * @param jar
	 * @return
	 */
	private PluginData getPluginDataFromFile(File jar){

		String name = ClientUtils.stripExtension(jar.getName());

		for(PluginData data : pluginData){
			System.out.println("NAME: "+data.getName());
			if(data.getName().equals(name)){
				return data;
			}
		}

		return null;
	}

	/**
	 * Checks if a plugin is enabled
	 * @param plugin
	 * @return
	 */
	public boolean pluginEnabled(PluginData plugin){
		return pluginWindows.containsKey(plugin);
	}

	/**
	 * Initialises a plugin from it's file and then adds the plugin to the mod system and window system
	 * @param plugin
	 * @throws MalformedURLException
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	public void addPlugin(PluginData plugin) throws MalformedURLException, ClassNotFoundException, InstantiationException, IllegalAccessException{

		File jar = new File(plugins, plugin.getFileName());

		Client.getClient().log("ADDING PLUGIN: "+jar.getPath());

		if(jar.isDirectory()){
			return;
		}

		if(isValid(jar)){

			Client.getClient().log("LOADING VALID JAR: "+jar.getName());

			String mainClass = "";

			JarFile jarFile = null;

			try {
				jarFile = new JarFile(jar);
			} catch (IOException e) {
				e.printStackTrace();
			}

			if(jarFile == null){
				Client.getClient().log("Error - jar file null");
			}

			JarEntry entry = jarFile.getJarEntry("plugin.json");

			if(entry != null){

				InputStream stream = null;

				try {
					stream = jarFile.getInputStream(entry);
				} catch (IOException e) {
					e.printStackTrace();
				}

				String pluginText = FileUtils.getStringFromInputStream(stream);
				JsonObject json = new JsonParser().parse(pluginText).getAsJsonObject();

				mainClass = json.get("main").getAsString();

			}else{
				mainClass = "net.yawk.mods.Plugin";
				Client.getClient().log("Error! Plugin manifest not found: "+plugin.getPluginIdentifier());
			}

			Client.getClient().log("Loading " + plugin.getPluginIdentifier() + " using main class "+mainClass);

			URI uri = jar.toURI();
			URL url = uri.toURL();
			URL[] urls = new URL[]{url};

			ClassLoader cl = new URLClassLoader(urls);
			Class register = Class.forName(mainClass, true, cl);

			PluginRegister reg = (PluginRegister) register.newInstance();
			cl.clearAssertionStatus();

			ArrayList<Mod> mods = new ArrayList<Mod>();

			//This allows the plugin to add hacks
			reg.init(Client.getClient(), mods);

			//This adds the window that will contain the plugin gui
			Window w;
			pluginWindows.put(plugin, w = new Window(plugin.getName(), Client.getClient().getModManager(), reg.getWidth(), 12));
			Client.getClient().getGui().windows.add(w);

			//This allows the plugin to add it's own components
			reg.addElements(Client.getClient(), w);

			//This puts the plugin hacks into the module system and plugin window gui
			for(Mod m : mods){

				Client.getClient().log("LOADING MOD: "+m.getName());

				if(m instanceof PluginMod){
					((PluginMod) m).setPluginData(plugin);
				}

				Client.getClient().getModManager().addPluginMod(m, plugin);
				w.addComponent(new ModButton(w, m));
			}

		}else{
			Client.getClient().log("INVALID JAR: "+jar.getName());
		}
	}

	/**
	 * Downloads a plugin from the website
	 * @param plugin
	 */
	public void downloadPlugin(PluginData plugin){

		Client.getClient().log("DOWNLOADING: "+plugin.getFileName());

		if(downloadThread != null && downloadThread.isAlive()){

			downloadQueue.add(plugin);

		}else{

			downloadQueue.add(plugin);

			downloadThread = new Thread(){

				public void run(){

					while(!downloadQueue.isEmpty()){

						PluginData data = downloadQueue.poll();

						File pluginFile = new File(plugins, data.getFileName());

						try {

							URL website = new URL(data.getFilePath());
							URL url = new URL(data.getFilePath());
							URLConnection hc = url.openConnection();
							hc.setRequestProperty("User-Agent", ClientUtils.USER_AGENT);
							ReadableByteChannel rbc;
							rbc = Channels.newChannel(hc.getInputStream());
							FileOutputStream fos = new FileOutputStream(pluginFile);
							fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
							fos.close();
							rbc.close();

						} catch (IOException e) {
							e.printStackTrace();
						}
						
						if(data.isPrivatePlugin()){
							try {
								addPlugin(data);
							} catch (MalformedURLException e) {
								e.printStackTrace();
							} catch (ClassNotFoundException e) {
								e.printStackTrace();
							} catch (InstantiationException e) {
								e.printStackTrace();
							} catch (IllegalAccessException e) {
								e.printStackTrace();
							}
						}
					}
				}

			};

			downloadThread.start();
		}
	}

	/**
	 * Removes a plugin from the mod system and the window system
	 * @param plugin
	 */
	public void removePlugin(PluginData plugin) {

		Client.getClient().log("REMOVING: "+plugin.getFileName());

		Client.getClient().getModManager().removePluginMods(plugin);

		Client.getClient().getGui().windows.remove(this.pluginWindows.get(plugin));
		pluginWindows.remove(plugin);
	}

	private boolean developerMode = true;

	/**
	 * This method is designed to stop people adding their own plugins which aren't approved by the website yawk.net
	 * In the future, plugins which aren't approved may be allowed, but this checking will stay enabled to protect client users from viruses
	 * 
	 * The hash check uses the SHA-1 algorithm to get the checksum of the plugin file we're trying to load
	 * It checks whether the file hash matches the hash of the file on the website (which is approved)
	 * 
	 * This should prevent any usage of modified plugin files
	 * Because any modified files will have a different checksum
	 * 
	 * The URL for hash checking is
	 * http://www.yawk.net/mods/valid.php
	 * 
	 * The parameters for hash checking on the website are:
	 * name = the name of the plugin file
	 * hash = the checksum of the plugin file
	 * 
	 * @param jar
	 * @return
	 */
	private boolean isValid(File jar) {

		if(developerMode){
			return true;
		}

		try {

			String hash = getHash(jar);
			Client.getClient().log("HASH: "+hash);
			Client.getClient().log("NAME: "+ClientUtils.stripExtension(jar.getName()));
			Document doc = Jsoup.connect("http://www.yawk.net/mods/valid.php?&name="+ClientUtils.stripExtension(jar.getName())+"&hash="+hash).userAgent(ClientUtils.USER_AGENT).get();
			Client.getClient().log("RESPONSE: "+doc.text());

			return doc.text().equalsIgnoreCase("true");

		} catch (IOException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		return false;
	}

	/**
	 * Gets an SHA-512 hash from the file given. This is used to check the hash against the website's saved hash
	 * @param jar
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws IOException
	 */
	private String getHash(File jar) throws NoSuchAlgorithmException, IOException{

		MessageDigest md = MessageDigest.getInstance("SHA-512");
		FileInputStream fis = new FileInputStream(jar);
		byte[] dataBytes = new byte[1024];

		int nread = 0;

		while ((nread = fis.read(dataBytes)) != -1) {
			md.update(dataBytes, 0, nread);
		};

		fis.close();

		byte[] mdbytes = md.digest();

		//convert the byte to hex format
		StringBuffer sb = new StringBuffer("");
		for (int i = 0; i < mdbytes.length; i++) {
			sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));
		}

		return sb.toString().toUpperCase();
	}
}
