package com.lzhn.utils.thread;

import com.lzhn.utils.thread.ThreadUtils.Worker;
import com.lzhn.utils.thread.ThreadUtils.Worker.OnWorkerRunningListener;

/**
 * 线程辅助类
 * 
 * @author lzhn
 * 
 */
public class ThreadUtils {
	/**
	 * 线程休眠
	 * 
	 * @param millis
	 *            休眠时间：毫秒单位
	 */
	public static void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 在<b>单独线程</b>中完成处理任务
	 * 
	 * @param onWorkerRunningListener
	 *            {@link Worker.OnWorkerRunningListener}
	 */
	public static void doWork(OnWorkerRunningListener onWorkerRunningListener) {
		new Thread(
				new Worker()
						.setOnWorkerRunningListener(onWorkerRunningListener))
				.start();
	}

	static class Worker implements Runnable {

		private OnWorkerRunningListener onWorkerRunningListener;

		public Worker setOnWorkerRunningListener(
				OnWorkerRunningListener onWorkerRunningListener) {
			this.onWorkerRunningListener = onWorkerRunningListener;
			return this;
		}

		@Override
		public void run() {
			// TODO 线程工作
			if (onWorkerRunningListener != null) {
				onWorkerRunningListener.onWorkerRunning();
			}
		}

		/**
		 * 线程工作监听回调接口
		 * 
		 * @author lzhn
		 * 
		 */
		interface OnWorkerRunningListener {
			/**
			 * 完成线程实际任务。此方法运行在{@link java.lang.Runnable#run()}方法之中
			 */
			void onWorkerRunning();
		}
	}

}
