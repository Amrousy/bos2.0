bosfore_app.controller("ctrlRead", ['$scope', '$http', function($scope, $http) {
	$scope.currentPage = 1; // 当前页数
	$scope.pageSize = 4; // 每页记录数
	$scope.totalPages = 0; // 总页数
	$scope.totalCount = 0; // 总记录数

	// 加载上一页数据
	$scope.prev = function() {
		$scope.selectPage($scope.currentPage - 1);
	}

	// 加载下一页数据
	$scope.next = function() {
		$scope.selectPage($scope.currentPage + 1);
	}

	// 加载指定页数据
	$scope.selectPage = function(page) {
		// page超出范围
		if($scope.totalPages != 0 && (page < 1 || page > $scope.totalPages)) {
			return;
		}
		$http({
			method: 'GET',
			url: 'data/promotion' + page + '.json',
			params: {
				"page": page, // 页码
				"pageSize": $scope.pageSize // 每页记录数
			}
		}).success(function(data, status, headers, config) {
			// 显示表格数据
			$scope.pageItems = data.pageItems;

			// 根据总记录数算出总页数
			$scope.totalCount = data.totalCount;
			$scope.totalPages = Math.ceil($scope.totalCount / $scope.pageSize);
			// 更新当前显示页码
			$scope.currentPage = page;

			// 固定显示10页
			var begin;
			var end;

			begin = page - 5;
			if(begin < 0) {
				begin = 1;
			}

			end = begin + 9;
			if(end > $scope.totalPages) {
				end = $scope.totalPages;
			}

			begin = end - 9;
			if(begin < 1) {
				begin = 1;
			}

			$scope.pageList = new Array();
			for(var i = begin; i <= end; i++) {
				$scope.pageList.push(i);
			}
		}).error(function(data, status, headers, config) {
			// 当响应以错误状态返回
			alert("出错，请联系管理员")
		});
	}

	$scope.isActivePage = function(page) {
		return page == $scope.currentPage;
	}
	
	// 发送请求显示第一页数据
	$scope.selectPage($scope.currentPage);
	
}]);