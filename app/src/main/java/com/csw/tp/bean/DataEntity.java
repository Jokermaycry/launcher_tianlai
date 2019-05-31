package com.csw.tp.bean;

import java.io.OutputStream;
import java.net.Socket;

import android.content.Context;

public class DataEntity {
	/*
	 * ��ǰ��ͻ���ͨ�ŵ�Socket
	 */
    public static Socket currentSocket=null;
    /*
     * ��ǰ����˵�Context
     */
    public static Context currentContext=null;
    public static OutputStream currentos = null;
}
