package Utilities;

import com.pi4j.io.i2c.I2CDevice;

public class EndianReaders {

	public enum Endianness {
		LITTLE_ENDIAN,
		BIG_ENDIAN
	}

	public static int readU8(I2CDevice device, int i2caddr, int reg) throws Exception {
		int result = 0;
		try {
			result = device.read(reg);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return result;
	}

	public static int readS8(I2CDevice device, int i2caddr, int reg) throws Exception {
		int result = 0;
		try {
			result = device.read(reg); 
			if (result > 127)
				result -= 256;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return result; 
	}

	public static int readU16LE(I2CDevice device, int i2caddr, int register) throws Exception {
		return EndianReaders.readU16(device, i2caddr, register, Endianness.LITTLE_ENDIAN);
	}

	public static int readU16BE(I2CDevice device, int i2caddr, int register, boolean verbose) throws Exception {
		return EndianReaders.readU16(device, i2caddr, register, Endianness.BIG_ENDIAN);
	}

	public static int readU16(I2CDevice device, int i2caddr, int register, Endianness endianness) throws Exception {
		int hi = EndianReaders.readU8(device, i2caddr, register);
		int lo = EndianReaders.readU8(device, i2caddr, register + 1);
		return ((endianness == Endianness.BIG_ENDIAN) ? (hi << 8) + lo : (lo << 8) + hi); 
	}

	public static int readS16(I2CDevice device, int i2caddr, int register, Endianness endianness) throws Exception {
		int hi = 0, lo = 0;
		if (endianness == Endianness.BIG_ENDIAN) {
			hi = EndianReaders.readS8(device, i2caddr, register);
			lo = EndianReaders.readU8(device, i2caddr, register + 1);
		} else {
			lo = EndianReaders.readU8(device, i2caddr, register);
			hi = EndianReaders.readS8(device, i2caddr, register + 1);
		}
		return ((hi << 8) + lo);
	}

	public static int readS16LE(I2CDevice device, int i2caddr, int register) throws Exception {
		return EndianReaders.readS16(device, i2caddr, register, Endianness.LITTLE_ENDIAN);
	}

	public static int readS16BE(I2CDevice device, int i2caddr, int register) throws Exception {
		return EndianReaders.readS16(device, i2caddr, register, Endianness.BIG_ENDIAN);
	}
}