package com.chengql.template.domain.query;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 
 * 
 * @author chengql
 * @email chengql@hanwei.cn
 * @date 2020-05-20 13:43:04
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
public class PoincollecdataQuery<T> extends Page<T> {

	private static final long serialVersionUID = 1L;
	
}
