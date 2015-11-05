package com.tdw.utils.module.worker;

public interface ITransWorker {
	String getName();

	String getAddress();

	String getSummary();

	boolean isWorking();

	boolean init(Object obj);

	boolean start();

	boolean stop();

	boolean restart();

	boolean release();
}
