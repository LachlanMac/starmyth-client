package com.pineconeindustries.shared.data;

public class Global {

	public static RUN_TYPE runType;
	
	public static enum RUN_TYPE {
		server, client, headless_server
	}
	
	public static void setRunType(RUN_TYPE type) {
		runType = type;
	}
	
	public static boolean isServer(){
		if(runType == RUN_TYPE.server || runType == RUN_TYPE.headless_server) {
			return true;	
		}else {
			return false;
		}
	}

}
