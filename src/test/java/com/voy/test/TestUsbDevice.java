package com.voy.test;

import java.util.List;

import javax.usb.UsbConfiguration;
import javax.usb.UsbConst;
import javax.usb.UsbDevice;
import javax.usb.UsbDeviceDescriptor;
import javax.usb.UsbDisconnectedException;
import javax.usb.UsbEndpoint;
import javax.usb.UsbException;
import javax.usb.UsbHostManager;
import javax.usb.UsbHub;
import javax.usb.UsbInterface;
import javax.usb.UsbInterfacePolicy;
import javax.usb.UsbNotActiveException;
import javax.usb.UsbNotOpenException;
import javax.usb.UsbPipe;
import javax.usb.UsbServices;

public class TestUsbDevice {

	private UsbEndpoint endPointIn;
	private UsbEndpoint endPointOut;
	private UsbPipe pipeIn;
	private UsbPipe pipeOut;
	private UsbDevice device;
	private UsbInterface interf;

	public static void main(String[] args) {
		TestUsbDevice usbDevice = new TestUsbDevice();
		usbDevice.init();

	}
	
	public void init(){
		try {
			UsbServices services = UsbHostManager.getUsbServices();
			UsbHub rootHub = services.getRootUsbHub();
			Short vendorId = 0x079B;
			Short productId = 0x0028;
			
			device = findDevice(rootHub, vendorId, productId);
			openPipes();
			
			byte[] statusCommands ={};
			sendByte(statusCommands);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	private UsbDevice findDevice(UsbHub hub, short vendorId, short productId) throws Exception {
		if (device != null) {
			return device;
		}
		for (UsbDevice device : (List<UsbDevice>) hub.getAttachedUsbDevices()) {
			if (device.getParentUsbPort() != null) {
				UsbDeviceDescriptor desc = device.getUsbDeviceDescriptor();
				if (desc.idVendor() == vendorId && desc.idProduct() == productId) {
					return device;
				}
				if (device.isUsbHub()) {
					device = findDevice((UsbHub) device, vendorId, productId);
					if (device != null) {
						return device;
					}
				}
			}
		}
		return null;
	}

	@SuppressWarnings("rawtypes")
	public String openPipes() throws Exception {
		String hexStatus = null;

		try {
			UsbConfiguration config = device.getActiveUsbConfiguration();
			List totalInterfaces = config.getUsbInterfaces();
			for (int i = 0; i < totalInterfaces.size(); i++) {
				interf = (UsbInterface) totalInterfaces.get(i);
				for (int j = 0; j < interf.getUsbEndpoints().size(); j++) {
					if (((UsbEndpoint) interf.getUsbEndpoints().get(j))
							.getDirection() == UsbConst.ENDPOINT_DIRECTION_OUT) {
						endPointOut = (UsbEndpoint) interf.getUsbEndpoints().get(j);
					}
					if (((UsbEndpoint) interf.getUsbEndpoints().get(j))
							.getDirection() == UsbConst.ENDPOINT_DIRECTION_IN) {
						endPointIn = (UsbEndpoint) interf.getUsbEndpoints().get(j);
					}
				}
			}

			if (endPointOut != null) {
				if (interf.isClaimed()) {
					interf.release();
				}
				if (!interf.isClaimed()) {
					interf.claim(new UsbInterfacePolicy() {
						@Override
						public boolean forceClaim(UsbInterface usbInterface) {
							return true;
						}
					});
				}
			}

			pipeIn = endPointIn.getUsbPipe();
			pipeOut = endPointOut.getUsbPipe();

			if (!pipeIn.isOpen()) {
				pipeIn.open();
			}
			if (!pipeOut.isOpen()) {
				pipeOut.open();
			}

			return hexStatus;
		} catch (Exception e) {
			System.out.println("erro ao abrir pipe");
			throw e;
		} finally {
		}
		
	}

	public void sendByte(byte[] bytes) throws InterruptedException {
		try {
			pipeOut.asyncSubmit(bytes);
			Thread.sleep(2000);
		} catch (UsbNotActiveException e) {
			e.printStackTrace();
		} catch (UsbNotOpenException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UsbDisconnectedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UsbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
		}
	}

}
