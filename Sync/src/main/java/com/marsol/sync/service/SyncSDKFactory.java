package com.marsol.sync.service;

public class SyncSDKFactory {
	
	public static SyncSDK createSyncSDK() {
		return new SyncSDKImpl();
	}
}
