/*******************************************************************************
 * This files was developed for CS4233: Object-Oriented Analysis & Design.
 * The course was taken at Worcester Polytechnic Institute.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Copyright ©2020 Andrew Whitney
 *******************************************************************************/

package escape;

import java.util.ArrayList;
import escape.exception.EscapeException;
import escape.observers.GameObserver;

/**
 * Description
 * @version Dec 6, 2020
 */
class TestGameObserver implements GameObserver
{

	ArrayList<String> messages;
	
	public TestGameObserver()
	{
		this.messages = new ArrayList<String>();
	}
	
	public void notify(String message)
	{
		messages.add(message);
	}
	
	public void notify(String message, Throwable cause)
	{
		messages.add(message);
	}
	
	protected String getLastMessage() {
		return messages.get(messages.size() - 1);
	}
	
	protected void printMessages() {
		for(String message : messages) {
			System.out.println(message);
		}
	}
	
	protected boolean hasMessage(String message) {
		if(messages.contains(message)) {
			return true;
		}
		return false;
	}

}
