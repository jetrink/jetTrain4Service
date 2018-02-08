'use strict';

export default {
	addRoutes: (stateProvider) => {
		stateProvider
			.state('app.account.tutor', {
				url: '/tutor',
				templateUrl: 'content/dashboard/tutor/tutor.dashboard.html',
				requireLogin: true
			})
	}
};
