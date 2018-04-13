package org.foxconn.MdDocument;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;
import org.foxconn.table.Table;

public class DocumentUtil {
	Logger logger = Logger.getLogger(DocumentUtil.class);
	private MdDocument getMdDocumentPartX(int x,int n,List<Table> list ){
		int total = list.size();
		int fromIndex = total*(x-1)/n;
		int toIndex = total*x/n;
		toIndex= toIndex>total?total:toIndex;
		List<Table> sublist =  list.subList(fromIndex, toIndex);
		logger.info("sublist from "+fromIndex+" to "+toIndex+" ;");
		MdDocument document =  new MdDocument(sublist);
		return document;
	} 
	public  List<MdDocument>  getMdDocumentsByPart(final int n,final List<Table> list){
		
		if(list.size()<n){
			return new ArrayList<MdDocument>();
		}
		int count =  list.size()%n==0?n:n+1;
		ExecutorService pool = Executors.newFixedThreadPool(count);
		final CountDownLatch cd = new CountDownLatch(count);
		logger.info("ThreadpoolSize:"+count);
		final List<MdDocument> documents= new ArrayList<MdDocument>();
		for(int i=1;i<=count;i++){
			final int index=i;
			Runnable runnale1 = new Runnable() {
				public void run() {
					MdDocument document= getMdDocumentPartX(index,n,list);
					document.setSortno(index);
					documents.add(document);
					cd.countDown();
					logger.info("countDown:"+index);
				}
			};
			pool.execute(runnale1);
		}
		try {
			cd.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		logger.info("return documents");
		pool.shutdown();
		return documents;
	}
}
