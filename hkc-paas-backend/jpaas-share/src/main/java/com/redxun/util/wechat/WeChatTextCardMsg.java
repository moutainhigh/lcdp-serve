package com.redxun.util.wechat;

public class WeChatTextCardMsg extends WeChatBaseMsg {
	
	
	private WeChatTextCard textcard;
	

	@Override
	public String getMsgtype() {
		return "textcard";
	}


	public WeChatTextCard getTextcard() {
		return textcard;
	}


	public void setTextcard(WeChatTextCard textcard) {
		this.textcard = textcard;
	}
	
}
