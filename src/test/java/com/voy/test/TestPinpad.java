package com.voy.test;

import java.lang.reflect.Field;
import java.util.Date;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import br.com.softwareexpress.sitef.JCliSiTefI;

public class TestPinpad {
	
	
	public static void refresh() {
		Field fieldSysPath;
		try {
			fieldSysPath = ClassLoader.class.getDeclaredField("sys_paths");
			fieldSysPath.setAccessible(true);
			fieldSysPath.set(null, null);
		} catch (Exception e) {
		}
	}
	
	
	public static void main(String[] args) {
		
		System.setProperty("java.library.path", "C:/Trabalho/workspaces-java/alo/voy-tef/dll");
		System.load( "C:/Trabalho/workspaces-java/alo/voy-tef/dll/libseppemv.dll");
		System.load( "C:/Trabalho/workspaces-java/alo/voy-tef/dll/CliSiTef32.dll");
		System.load( "C:/Trabalho/workspaces-java/alo/voy-tef/dll/CliSiTef32I.dll");
		System.load( "C:/Trabalho/workspaces-java/alo/voy-tef/dll/jCliSiTefI.dll");
		refresh();
		
		int status;
		int nextCmd;
		JCliSiTefI clisitef = new JCliSiTefI();
		
		
		clisitef.setEnderecoSiTef("sitefp.voy.com.br");
		//clisitef.setEnderecoSiTef("127.0.0.1");
		clisitef.setCodigoLoja("24000182");
		//clisitef.setCodigoLoja("00000000");
		clisitef.setNumeroTerminal("00003138");
		clisitef.setConfiguraResultado(0);
		
		status = clisitef.configuraIntSiTefInterativo();
		System.out.println("configuraIntSiTefInterativo()"+status);
		
		//status = clisitef.abrePinPad();
		//System.out.println("abrePinPad()"+status);

		clisitef.setFuncaoSiTef(1);
		clisitef.setValor("0");
		clisitef.setNumeroCuponFiscal("0");
		Date dt = new Date();
		clisitef.setDataFiscal(DateFormatUtils.format(dt, "yyyyMMdd"));
		clisitef.setHorario(DateFormatUtils.format(dt, "HHmmss"));
		clisitef.setOperador("");
		clisitef.setRestricoes("");
		status = clisitef.iniciaFuncaoSiTefInterativo();
		System.out.println("iniciaFuncaoSiTefInterativo()"+status);

		do{
			status = clisitef.continuaFuncaoSiTefInterativo();
			
			System.out.println("continuaFuncaoSiTefInterativo()"+status);
			nextCmd = clisitef.getProximoComando();
			System.out.println("Buffer: "+clisitef.getBuffer());
			System.out.println("Proximo Comando:" +nextCmd);
			clisitef.setFuncaoSiTef(nextCmd);
			
		}while(status == 10000);
		
		
		
		status = clisitef.fechaPinPad();
		System.out.println("fechaPinPad()"+status);
		
		
		
		
		
	}
}