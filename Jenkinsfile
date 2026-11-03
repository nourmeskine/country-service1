pipeline {
    agent any
    tools {
        maven 'M2_HOME'
    }
    stages {
        stage('Checkout code') {
            steps {
                // REMPLACER par l'URL de TON fork GitHub
                git branch: 'master', url: 'https://github.com/nourmeskine/country-service1'
            }
        }
        stage('Build maven') {
            steps {
                sh 'mvn clean install'
            }
        }
        stage('Deploy using Ansible playbook') {
            steps {
                withCredentials([
                    string(credentialsId: 'ansible-vault-pass', variable: 'VAULT_PASS'),
                    file(credentialsId: 'kubeconfig-file', variable: 'KUBECONFIG_FILE')
                ]) {
                    sh '''
                        trap 'rm -f .vault_pass' EXIT
                        printf '%s' "$VAULT_PASS" > .vault_pass
                        ansible-playbook -i hosts playbookCICD.yml \
                            --vault-password-file .vault_pass \
                            -e kubeconfig_path="$KUBECONFIG_FILE"
                    '''
                }
            }
        }
    }
    post {
        always {
            cleanWs()
        }
        success {
            echo 'Ansible playbook executed successfully!'
        }
        failure {
            echo 'Ansible playbook execution failed!'
        }
    }
}
