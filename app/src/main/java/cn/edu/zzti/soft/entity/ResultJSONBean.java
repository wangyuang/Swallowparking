package cn.edu.zzti.soft.entity;

import java.io.Serializable;
import java.util.UUID;

public class ResultJSONBean implements Serializable{

	private static final long serialVersionUID = -324722745410120279L;

	private String token;
	private String result;
	private String errorMsg;

	public ResultJSONBean() {
	}

	public ResultJSONBean(String token, String result, String errorMsg) {
		super();
		this.token = token;
		this.result = result;
		this.errorMsg = errorMsg;
	}


	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

}
