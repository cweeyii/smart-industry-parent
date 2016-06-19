package com.cweeyii.quartz.framework.dao.mapper;


import com.cweeyii.quartz.framework.domain.QrtzJobDetails;

import java.util.List;

/**
 * Created by wenyi on 16/2/27.
 * Email:caowenyi@meituan.com
 */
public interface QrtzJobDetailsMapper {
	
	 List<QrtzJobDetails> getAllJobDetails();

}
