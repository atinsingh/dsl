import groovy.json.JsonSlurper

def basePath = 'Multi-branch-folder'
def repo = 'atinsingh/pragrab10'

folder(basePath) {
    description 'Another Description.'
}

URL branchUrl = "https://api.github.com/repos/$repo/branches".toURL()
List branches = new JsonSlurper().parse(branchUrl.newReader())

branches.each { branch ->

    def safeBranchName = branch.name.replaceAll('/', '-')

    //folder "$basePath/$safeBranchName"

    job("$basePath/$branch.name-test-compile") {
        scm {
             git('git://github.com/atinsingh/pragrab10.git', branch.name)
        }
        triggers {
            scm '* * * * *'
        }
        steps {
            maven('clean compile')
        }
    }

    job("$basePath/$branch.name-test-build") {
         scm {
             git('git://github.com/atinsingh/pragrab10.git', branch.name)
        }
        triggers {
            scm '* * * * *'
        }
        steps {
            maven('test')
        }
    }
}
