'use strict';

export default {
	addRoutes: (stateProvider) => {
		stateProvider
			.state('app.account.manager', {
				url: '/manager',
				templateUrl: 'content/dashboard/manager/manager.dashboard.html',
				requireLogin: true
			})
	}
};
