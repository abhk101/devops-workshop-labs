xld {

    define(
            forInfrastructure: 'Infrastructure/RedHat'
    ) {
        infrastructure('openshift-cluster', 'openshift.Server') {
            serverUrl = 'https://kubernetes.default.svc:443'
            openshiftToken = '' //Encrypted value removed for export
            verifyCertificates = false
            infrastructure('coolstore-prod', 'openshift.Project') {
                container = ref('Infrastructure/RedHat/openshift-cluster')
                projectName = 'coolstore-prod'
            }
            infrastructure('coolstore-qa', 'openshift.Project') {
                container = ref('Infrastructure/RedHat/openshift-cluster')
                projectName = 'coolstore-qa'
            }
        }
    }
    define(
            forEnvironments: 'Environments/RedHat'
    ) {
        environment('PROD') {
            members = [
                    ref('Infrastructure/RedHat/openshift-cluster/coolstore-prod')
            ]
            dictionaries = [
                    ref('Environments/RedHat/prod.configuration'),
                    ref('Environments/RedHat/global.configuration')
            ]
        }
        environment('QA') {
            members = [
                    ref('Infrastructure/RedHat/openshift-cluster/coolstore-qa')
            ]
            dictionaries = [
                    ref('Environments/RedHat/qa.configuration'),
                    ref('Environments/RedHat/global.configuration')
            ]
        }
        dictionary('global.configuration', [
                'REGISTRY'                   : 'docker-registry.default.svc:5000',
                'default.periodSeconds'      : '30',
                'default.timeoutSeconds'     : '20',
                'default.initialDelaySeconds': '20'
        ])
        dictionary('prod.configuration', [
                'catalog.periodSeconds'        : '{{default.periodSeconds}}',
                'catalog.initialDelaySeconds'  : '{{default.initialDelaySeconds}}',
                'inventory.periodSeconds'      : '{{default.periodSeconds}}',
                'catalog.db.password'          : 'prodpassword',
                'CATALOG_DB_PASSWORD'          : '{{catalog.db.password}}',
                'MONGODB_ADMIN_PASSWORD'       : 'prodpassword',
                'inventory.initialDelaySeconds': '{{default.initialDelaySeconds}}',
                'catalog.db.username'          : 'tiger',
                'CATALOG_DB_USERNAME'          : '{{catalog.db.username}}',
                'inventory.timeoutSeconds'     : '30',
                'catalog.timeoutSeconds'       : '30',
                'env'                          : 'prod'
        ])
        dictionary('qa.configuration', [
                'catalog.periodSeconds'        : '{{default.periodSeconds}}',
                'catalog.initialDelaySeconds'  : '{{default.initialDelaySeconds}}',
                'inventory.periodSeconds'      : '{{default.periodSeconds}}',
                'catalog.db.password'          : 'qapassword',
                'CATALOG_DB_PASSWORD'          : '{{catalog.db.password}}',
                'MONGODB_ADMIN_PASSWORD'       : 'qapassword',
                'inventory.initialDelaySeconds': '{{default.initialDelaySeconds}}',
                'catalog.db.username'          : 'tiger',
                'CATALOG_DB_USERNAME'          : '{{catalog.db.username}}',
                'inventory.timeoutSeconds'     : '20',
                'catalog.timeoutSeconds'       : '20',
                'env'                          : 'qa'
        ])
    }
}