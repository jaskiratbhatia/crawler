package com.crawler;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Iterator;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Crawler {
	private static int MAX_DEPTH = 3;
	private HashSet<String> urls = new HashSet<>();
	private static String hostName;
	private Node finalNodes = new Node();

	public void crawlurl(String url, Node parent, int max) {
		MAX_DEPTH = max;
		getPageURLs(url, 0, parent);
	}

	private void getPageURLs(String URL, int depth, Node parent) {
		if ((!urls.contains(URL) && (depth < MAX_DEPTH))) {
			System.out.println(">> Depth: " + depth + " [" + URL + "]");
			Connection connection = Jsoup.connect(URL);
			try {
				urls.add(URL);
				Document document = connection.get();
				Elements linksOnPage = document.select("a[href]");
				Node n = new Node();
				System.out.println("status codes for URL " + URL + " " + connection.response().statusCode());
				if (depth == 0) {
					parent.setData(URL);
					parent.setParent(null);
					parent.setStatus(connection.response().statusCode());
					n = parent;
				} else {
					n.setData(URL);
					n.setStatus(connection.response().statusCode());
					parent.addChild(n);
					n.setParent(parent);
				}
				depth++;
				for (Element page : linksOnPage) {
					URL u = new URL(page.attr("abs:href"));
					if (u.getHost().equals(hostName)) {
						getPageURLs(page.attr("abs:href"), depth, n);
					}
				}
			} catch (IOException e) {
				Node n = new Node();
				n.setData(URL);
				n.setStatus(408);
				parent.addChild(n);
				n.setParent(parent);
				System.out.println("exception caused");
				e.printStackTrace();
			}
		}
	}

	void printDepthFirstOrder(Node node, int depth) {
		if (node == null) {
			return;
		}

		// first recur on left subtree
		if (node.getChildren() == null || node.getChildren().isEmpty()) {
			return;
		} else {
			depth++;
			Iterator<Node> it = node.getChildren().iterator();
			while (it.hasNext()) {
				Node n = it.next();
				printDepthFirstOrder(n, depth);
				//System.out.println("depth of child = " + depth + " child url is " + n.getData() + " my parent is "
				//		+ n.getParent().getData());
				if (n.getStatus() != 200) {
					System.out.println("omg non 200 status");
					System.out.println("depth of child = " + depth + " child url is " + n.getData() + " my parent is "
                                                + n.getParent().getData() + " status = " + n.getStatus());
				}
			}
		}
		if (node.getParent() == null) {
			System.out.println("depth 0 " + node.getData() + " my childer are " + node.getChildren().size());
		}
	}

	public static void main(String[] args) {
		if(args.length < 2) {
			System.out.println("2 args expected, URL as string with format http://host:port/and DEPTH in integer");
		}
		
		String url = args[0].toString();
		int maxDepth = Integer.parseInt(args[1]);
		MAX_DEPTH = maxDepth;
		Crawler myWebCrawler = new Crawler();
		URL u;

		try {
			u = new URL(url);
			System.out.println("host name " + u.getHost());
			hostName = u.getHost();
			myWebCrawler.crawlurl(u.toString(), myWebCrawler.finalNodes, MAX_DEPTH);
			myWebCrawler.printDepthFirstOrder(myWebCrawler.finalNodes, 0);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

}

