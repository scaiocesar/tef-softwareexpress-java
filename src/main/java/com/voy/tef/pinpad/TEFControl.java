package com.voy.tef.pinpad;

import java.lang.reflect.Field;
import java.util.Date;

import org.apache.commons.lang3.time.DateFormatUtils;

import br.com.softwareexpress.sitef.JCliSiTefI;

public class TEFControl {
	
	private String hostSitef;
	private String codLoja;
	private String numTerminal;
	private JCliSiTefI clisitef;
	
	private static void refresh() {
		Field fieldSysPath;
		try {
			fieldSysPath = ClassLoader.class.getDeclaredField("sys_paths");
			fieldSysPath.setAccessible(true);
			fieldSysPath.set(null, null);
		} catch (Exception e) {
		}
	}
	
	public TEFControl(String hostSitef, String codLoja, String numeroTerminal){
		System.setProperty("java.library.path", "C:/Trabalho/workspaces-java/alo/voy-tef/dll");
		System.load( "C:/Trabalho/workspaces-java/alo/voy-tef/dll/libseppemv.dll");
		System.load( "C:/Trabalho/workspaces-java/alo/voy-tef/dll/CliSiTef32.dll");
		System.load( "C:/Trabalho/workspaces-java/alo/voy-tef/dll/CliSiTef32I.dll");
		System.load( "C:/Trabalho/workspaces-java/alo/voy-tef/dll/jCliSiTefI.dll");
		refresh();
		
		clisitef = new JCliSiTefI();
		clisitef.setEnderecoSiTef(hostSitef);
		clisitef.setCodigoLoja(codLoja);
		clisitef.setNumeroTerminal(numeroTerminal);
		//defaultConfig
		clisitef.setConfiguraResultado(0);
		
	}



	
	public String getHostSitef() {
		return hostSitef;
	}

	public void setHostSitef(String hostSitef) {
		this.hostSitef = hostSitef;
	}

	public String getCodLoja() {
		return codLoja;
	}

	public void setCodLoja(String codLoja) {
		this.codLoja = codLoja;
	}

	public String getNumTerminal() {
		return numTerminal;
	}

	public void setNumTerminal(String numTerminal) {
		this.numTerminal = numTerminal;
	}

}
