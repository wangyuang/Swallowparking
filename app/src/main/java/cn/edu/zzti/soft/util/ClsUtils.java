package cn.edu.zzti.soft.util;

import java.lang.reflect.Method;

import android.bluetooth.BluetoothDevice;
import android.util.Log;

@SuppressWarnings("unchecked")
public class ClsUtils {
	// 因为是其中BluetoothDevice 中是hide的需要其中的代理类。来设置其中的setPin这个方法
	 @SuppressWarnings("rawtypes")
	public static boolean setPin(Class btClass, BluetoothDevice btDevice,
            String str) throws Exception{
        try
        {
			Method removeBondMethod = btClass.getDeclaredMethod("setPin",
                    new Class[]
                    {byte[].class});
            Boolean returnValue = (Boolean) removeBondMethod.invoke(btDevice,
                    new Object[]
                    {str.getBytes()});
            Log.d("returnValue", "setPin is success " +btDevice.getAddress()+ returnValue.booleanValue());
        }
        catch (SecurityException e)
        {
            // throw new RuntimeException(e.getMessage());
            e.printStackTrace();
        }
        catch (IllegalArgumentException e)
        {
            // throw new RuntimeException(e.getMessage());
            e.printStackTrace();
        }
        catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return true; 

    } 

}
