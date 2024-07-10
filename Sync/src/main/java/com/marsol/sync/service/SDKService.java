package com.marsol.sync.service;

import com.marsol.sync.model.TSDKDeviceInfo;
import com.marsol.sync.util.IPUtils;
//import com.marsol.sync.util.HEXUtils;
import com.marsol.sync.util.DataTypeConverter;
import com.marsol.sync.util.OperationTypeConverter;
import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.Callback;
import java.net.UnknownHostException;

public class SDKService {

	
	//Interfaz para el SDK
	public interface SyncSDK extends Library{
		//SyncSDK INSTANCE = Native.load("SyncSDK", SyncSDK.class);
		SyncSDK INSTANCE = Native.load("SyncSDK.dll", SyncSDK.class);
		boolean SDK_Initialize();
		int SDK_GetLastTaskError();
		void SDK_Finalize();
		TSDKDeviceInfo SDK_GetDeviceInfo(long addr);
		Pointer SDK_ExecTaskA(long addr, int operationType, String dataType, String fileName, ProgressCallback callback, Pointer userData);
	}
	
	//Interfaz para el callback del progreso
	
	public interface ProgressCallback extends Callback{
		void invoke(int progress);
	}
	
	//Método para inicializar el SDK
	
	public boolean initializeSDK(){
		return SyncSDK.INSTANCE.SDK_Initialize();	
	}
	
	//Método para soltar el SDK
	
	public void releaseSDK() {
		
		SyncSDK.INSTANCE.SDK_Finalize();
	}
	
	//Metodo para obtener informacion del dispositivo
	
	public TSDKDeviceInfo getDeviceInfo(String ip) {
		long ipCardinal;
		try {
			ipCardinal = IPUtils.ipToCardinal(ip);
		} catch (UnknownHostException e) {
			throw new RuntimeException("Dirección ip inválida: "+ip);
		}
		return SyncSDK.INSTANCE.SDK_GetDeviceInfo(ipCardinal);
	}
	
	//Metodo para ejecutar tarea con callback de progreso
	
	public Pointer executeTaskA(String ip, String operationType, String dataType, String filename, ProgressCallback callback, Pointer userData) {
		long ipCardinal;
		try {
			ipCardinal = IPUtils.ipToCardinal(ip);
		} catch(UnknownHostException e) {
			throw new RuntimeException("Dirección ip inválida: "+ip);
		}
		int operationTypeConvert = OperationTypeConverter.convertOperationType(operationType);
		String dataTypeConvert = DataTypeConverter.convertDataType(dataType);
		
		return SyncSDK.INSTANCE.SDK_ExecTaskA(ipCardinal, operationTypeConvert, dataTypeConvert, filename, callback, userData);
	}
}
