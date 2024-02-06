
package com.tenco.bank.dto;


import lombok.Data;

@Data
public class Profile {

	private String nickname;
	private String thumbnailImageUrl;
	private String profileImageUrl;
	private Boolean isDefaultImage;


}
