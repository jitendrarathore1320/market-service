package com.advantal.responsepayload;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SukukIndicesRobotsTask {

	private Integer totalCount;
	private Integer pageNumber;
	private Boolean hasMore;
	private List<IndicesRobotItems>items;

}
