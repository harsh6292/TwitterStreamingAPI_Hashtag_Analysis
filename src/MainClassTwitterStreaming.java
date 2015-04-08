import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.text.html.MinimalHTMLWriter;

import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

public class MainClassTwitterStreaming {

	private final static String CONSUMER_KEY = "CmaDfowibkOFmVxi6rJmV3d4O";
	private final static String CONSUMER_SECRET = "GKVYvpRzulGGGfY5Ik76QP0M2Nk7JPYOMHD1rXqCIMIVqYtHCq";
	
	private final static String ACCESS_TOKEN = "70961546-nDhvrh1NOCsXZmMqZ4LO5Dat3Yy81wCM4YTi8dDCy";
	private final static String ACCESS_SECRET = "wGQ4KY9delz3fXGbAbJBE9lcMxQajKzCCXCuvpogemTmH";
	
	public final static int MAX_CHARS = 26; 
	
	ConcurrentHashMap<String, Integer> TagsCount = new ConcurrentHashMap<String, Integer>();


	//Class to build configuration with Consumer Key, Secret, Access Toker and Secret	
	class BuildConfiguration
	{
		ConfigurationBuilder cb;
		
		public BuildConfiguration() {
			// TODO Auto-generated constructor stub
			
			cb = new ConfigurationBuilder();
		}
		
		ConfigurationBuilder getConfiguration()
		{
			cb.setDebugEnabled(true);
			cb.setOAuthConsumerKey(CONSUMER_KEY);
			cb.setOAuthConsumerSecret(CONSUMER_SECRET);
			cb.setOAuthAccessToken(ACCESS_TOKEN);
			cb.setOAuthAccessTokenSecret(ACCESS_SECRET);
			
			return cb;
		}
	}
	
		
	class StatusListenerAdapter implements StatusListener
	{

		@Override
		public void onException(Exception arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onDeletionNotice(StatusDeletionNotice arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onScrubGeo(long arg0, long arg1) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onStallWarning(StallWarning arg0) {
			// TODO Auto-generated method stub
			
		}


		//OnStatus method that will capture each status it receives from Twitter stream
		@Override
		public void onStatus(Status arg0) {
			// TODO Auto-generated method stub
			
			HashtagEntity[] hashTags = arg0.getHashtagEntities();

			//Loop to check if hashtag contains the previous word (made it lower-case)
			//so that same hashtags are put in same entry.
			for(HashtagEntity hashTag : hashTags)
			{
				if(hashTag != null && hashTag.getText().matches("[a-zA-Z0-9]+"))
				{
					Integer val = 0;	        		
					if( (val = TagsCount.get(hashTag.getText().toLowerCase())) != null)
					{
						TagsCount.put(hashTag.getText().toLowerCase(), (val.intValue()+1));
					}
					else
					{
						TagsCount.put(hashTag.getText().toLowerCase(), 1);
					}
							
					//insertInTrieAndHeap(hashTag.getText(), root, minHeap);
				}
			}
		}

		@Override
		public void onTrackLimitationNotice(int arg0) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	

	//Class that will display the rop-K hashtags
	class DisplayTopHashTags extends Thread
	{
		long seconds;
		int  MaxEntry_K;
		
		public DisplayTopHashTags(long inputSeconds, int sizeK) {
			// TODO Auto-generated constructor stub
			seconds = inputSeconds;
			MaxEntry_K = sizeK;
		}
		
		
		public void run()
		{
			while(true)
			{
				try
				{
					Thread.sleep(seconds*1000);
				
					ValueComparator compareByValue =  new ValueComparator(TagsCount);
					TreeMap<String, Integer> sortedTreeMap = new TreeMap<String, Integer>(compareByValue);
					sortedTreeMap.putAll(TagsCount);
					
					TagsCount.clear();
					
					int k = 0;
					System.out.println();
					System.out.println();
					System.out.println("Top-"+MaxEntry_K+" Hashtags in "+seconds+" seconds are:");
					System.out.format("\n%-32s    %-10s", "Hashtag", "Frequency");
					System.out.println();
					System.out.println("----------------------------------------------");
					for(Map.Entry<String, Integer> entry : sortedTreeMap.entrySet())
					{
						if( k < MaxEntry_K)
						{
							System.out.format("%-32s    %5d\n", entry.getKey(), entry.getValue());
							k++;
						}
						else
							break;
					}
					System.out.println();
				}
				
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
		}
	}
	
	
	//Class to provide a comparator for the frequency of words occuring in HashMap
	//that will be ordered while inserting in TreeMap
	class ValueComparator implements Comparator<String>
	{
	   	Map<String, Integer> hashMap;
	    	public ValueComparator(Map<String, Integer> base)
		{
	        	hashMap = base;
	    	}

	    	public int compare(String str1, String str2)
		{
			if (hashMap.get(str1) >= hashMap.get(str2))
				return -1;
	        	else
				return 1;
		}
	}
	
	
	
	public static void main(String[] args) {
		
		try
		{
			//create object of main class			
			MainClassTwitterStreaming mainClassObject = new MainClassTwitterStreaming();
		
			//Build configuration that contains all the Tokens and secrets
			BuildConfiguration buildConf = mainClassObject.new BuildConfiguration();
			ConfigurationBuilder cb = buildConf.getConfiguration();

			//Build a twitter stream object from configuration. This will be used to authenticate
			//as well as stream tweets from Twitter.
			TwitterStream twitterStream = new TwitterStreamFactory(cb.build()).getInstance();
		
			//Add listener to TwitterStream so that it can listen on to public statues
			twitterStream.addListener(mainClassObject.new StatusListenerAdapter());
		
			//the sample() method captures random tweets from Twitter.
			twitterStream.sample();
		
			long seconds = Integer.parseInt(args[0]);
			int maxK = Integer.parseInt(args[1]);

			//Display thread that will display top-K hashtags after every 's' seconds
			Thread displayThread = new Thread(mainClassObject.new DisplayTopHashTags(seconds, maxK));
			displayThread.start();
		}

		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
