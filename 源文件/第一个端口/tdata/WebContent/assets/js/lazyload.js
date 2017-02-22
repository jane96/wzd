// lazyload config

app
/**
 * jQuery plugin config use ui-jq directive , config the js and css files that required
 * key: function name of the jQuery plugin
 * value: array of the css js file located
 */
    .constant('JQ_CONFIG', {
    	
        script: {
        	'saleRecordCtrl':['/tdata/assets/js/controllers/saleRecordCtrl.js'],
        	'datepicker':['/tdata/assets/js/plugins/daterpicker-bs3.css','/tdata/assets/js/plugins/daterangepicker.js',
        	              'moment.min.js']

        },
        //*** angularJS Modules
        modules: [
            
        ]
    }
)
.constant('tid',"");
