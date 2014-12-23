package com.lzhn.utils.thread;

import android.os.Handler;
import android.os.Looper;

/**
 * 计时器
 * 
 * @author lzhn
 * 
 */
public class Timer {
	public static final String TAG = "Timer";
	/** 工作线程 */
	private final WorkerThread workerThread;
	/** 计时器工作监听器 */
	private OnTimerListener onTimerListener;
	/** 消息处理器 */
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			int count = msg.arg1;
			switch (msg.what) {
			case WorkerThread.WHAT_RUNNING:
				onTimerListener.onTimerRunning(count);
				break;
			case WorkerThread.WHAT_STOP:
				onTimerListener.onTimerStoped(count);
				break;
			case WorkerThread.WHAT_FINISH:
				onTimerListener.onTimerFinished(count);
				break;

			default:
				break;
			}
		};
	};

	private Timer(int count) {
		this();
		setCount(count);
	}

	private Timer() {
		this.workerThread = new WorkerThread(handler);
	}

	/**
	 * 生成一个计时器
	 * 
	 * @param count
	 *            计时器计时，以秒为单位
	 * @return
	 */
	public static Timer getTimer(int count) {
		return new Timer(count);
	}

	/**
	 * 生成一个计时器、默认计时60秒
	 * 
	 * @return
	 */
	public static Timer getTimer() {
		return new Timer();
	}

	/**
	 * 设定计时器计时长度（s）
	 * 
	 * @param count
	 * @return
	 */
	public Timer setCount(int count) {
		this.workerThread.setCount(count);
		return this;
	}

	public Timer setOnTimerListener(OnTimerListener onTimerListener) {
		this.onTimerListener = onTimerListener;
		return this;
	}

	public Handler getHandler() {
		return handler;
	}

	/** 计时器是否启动 */
	public boolean isStarted() {
		return this.workerThread.isStarted();
	}

	/** 计时器是否正在运行 */
	public boolean isRuning() {
		return this.workerThread.isRuning();
	}

	/**
	 * 启动定时器
	 */
	public void startTimer() {
		this.workerThread.start();
	}

	/**
	 * 终止定时器任务
	 */
	public void stopTimer() {
		this.workerThread.stopWork();
	}

	/**
	 * 定时器完成工作
	 */
	public void finishTimer() {
		this.workerThread.finishWork();
	}

	private class WorkerThread extends Thread {
		private static final int WHAT_STOP = 0;
		private static final int WHAT_RUNNING = 1;
		private static final int WHAT_FINISH = 2;
		private Handler handler;
		private int count = 60;
		private boolean isStarted = false;
		private boolean isStoped = false;

		private WorkerThread(int count, Handler handler) {

			this(handler);
			setCount(count);
		}

		private WorkerThread(Handler handler) {
			this.handler = handler;
		}

		private void setCount(int count) {
			if (count < 0)
				count = 0;
			this.count = count;
		}

		private boolean isStarted() {
			return isStarted;
		}

		private boolean isRuning() {
			return count > 0 && this.isAlive();
		}

		private void stopWork() {
			isStoped = true;
		}

		private void finishWork() {
			setCount(0);
		}

		@Override
		public void run() {
			super.run();
			isStarted = true;
			Looper.prepare();
			while (!isStoped && count > 0) {
				count--;
				handler.obtainMessage(WHAT_RUNNING, count, 0).sendToTarget();
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			if (isStoped) {
				handler.obtainMessage(WHAT_STOP, count, 0).sendToTarget();
			} else {
				handler.obtainMessage(WHAT_FINISH, count, 0).sendToTarget();
			}
			Looper.loop();
		}
	}

	/**
	 * 计时器工作监听器接口
	 * 
	 * @author lzhn
	 * 
	 */
	public interface OnTimerListener {
		void onTimerRunning(int count);

		void onTimerFinished(int count);

		void onTimerStoped(int count);
	}

}
