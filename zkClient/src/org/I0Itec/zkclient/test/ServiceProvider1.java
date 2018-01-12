package org.I0Itec.zkclient.test;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.ZkClient;
import org.apache.zookeeper.CreateMode;

public class ServiceProvider1 {
	private static String PATH = "/configcenter";
	
	private static String zkserver = "116.31.122.23:2181";
	
	private static List<String> serversList = new ArrayList<String>();
	
	public static void startUp(String serviceName) throws Exception{
		
		String servicePath = PATH+"/"+serviceName;
		ZkClient client = new ZkClient(zkserver);
		if(!client.exists(PATH)){
			client.createPersistent(PATH);
		}
		//createListener(client,servicePath);
		
		
		boolean serviceExits = client.exists(servicePath);
		if(!serviceExits){
			client.createPersistent(servicePath);
		}
		
		InetAddress addr = InetAddress.getLocalHost();
		String ip = addr.getHostAddress().toString();
		client.createEphemeral(servicePath+"/"+ip+":8080");
		
		
	}
	
	
	public static void createListener(ZkClient client,String servicePath){
		client.subscribeChildChanges(servicePath, new IZkChildListener() {
			@Override
			public void handleChildChange(String parentPath, List<String> currentChilds)throws Exception {
				serversList = currentChilds;
				System.out.println("******************currentChilds*********************");
				for(String node:serversList){
					System.out.println(node);
				}
			}
		});
	
		
	}
	
	
	
	
	public static void main(String[] args) throws Exception {
		startUp("entSigncar");
		Thread.sleep(Long.MAX_VALUE);
	}
}
