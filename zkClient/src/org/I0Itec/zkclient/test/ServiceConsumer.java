package org.I0Itec.zkclient.test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.LockSupport;

import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.ZkClient;

public class ServiceConsumer {
	private static String PATH = "/configcenter";
	
	private static List<String> serversList = new ArrayList<String>();
	
	public static void startUp(String serviceName) throws Exception{
		String zkserver = "116.31.122.23:2181";
		String servicePath = PATH+"/"+serviceName;
		ZkClient client = new ZkClient(zkserver);
		if(!client.exists(PATH)){
			client.createPersistent(PATH);
		}
		createListener(client,servicePath);
		
		
		boolean serviceExits = client.exists(servicePath);
		if(serviceExits){
			List<String> children = client.getChildren(servicePath);
			for (int i = 0; i < children.size(); i++) {
				System.out.println(children.get(i));
			}
		}else{
			throw new RuntimeException("service not exist");
		}
		
		
		
		
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
		LockSupport.park();
	}

}
