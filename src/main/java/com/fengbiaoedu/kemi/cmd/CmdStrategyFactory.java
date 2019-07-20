package com.fengbiaoedu.kemi.cmd;

public class CmdStrategyFactory {
	
	public BaseCmdStrategy createCmdStrategy(String cmd)   {
		
		cmd = this.getCmdClassName(cmd);
		Object cmdStrategy = null;
		//利用反射创建实例
		try {
			Class c = Class.forName(cmd);
			cmdStrategy = c.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return (BaseCmdStrategy) cmdStrategy;
	}
	
	public String getCmdClassName(String cmd) {
		String[] cmdArray = cmd.split(",");
		return "com.fengbiaoedu.kemi.cmd."+cmdArray[1];
	}

}
