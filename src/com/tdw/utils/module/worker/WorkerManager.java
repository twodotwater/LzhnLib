package com.tdw.utils.module.worker;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class WorkerManager {
	private static HashMap<String, ITransWorker> workerMap = new HashMap<String, ITransWorker>();

	public static HashMap<String, ITransWorker> getWorkerMap() {
		return workerMap;
	}

	public static ArrayList<ITransWorker> getWorkerList() {
		ArrayList<ITransWorker> workerList = new ArrayList<ITransWorker>();
		workerList.addAll(workerMap.values());
		return workerList;
	}

	public static ArrayList<ITransWorker> getWorkingList() {
		ArrayList<ITransWorker> workerList = new ArrayList<ITransWorker>();
		for (ITransWorker transWorker : workerMap.values()) {
			if (transWorker.isWorking()) {
				workerList.add(transWorker);
			}
		}
		return workerList;
	}

	public static ITransWorker getTransWorker(String key) {
		return workerMap.get(key);
	}

	public static ITransWorker addTransWorker(String key, ITransWorker transWorker) {
		return workerMap.put(key, transWorker);
	}

	public static ITransWorker removeTransWorker(String key) {
		return workerMap.remove(key);
	}
}
