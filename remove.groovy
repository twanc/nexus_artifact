
import org.sonatype.nexus.repository.storage.Asset
import org.sonatype.nexus.repository.storage.Query
import org.sonatype.nexus.repository.storage.StorageFacet
import org.sonatype.nexus.BlobStoreApi


import groovy.json.JsonOutput
import groovy.json.JsonSlurper

def request = new JsonSlurper().parseText(args)
assert request.repoName: 'repoName parameter is required'
assert request.startDate: 'startDate parameter is required'

log.info("Gathering Asset list for repository: ${request.repoName} as of startDate: ${request.startDate}")

def repo = repository.repositoryManager.get(request.repoName)
StorageFacet storageFacet = repo.facet(StorageFacet)
def tx = storageFacet.txSupplier().get()

tx.begin()

Iterable<Asset> assets = tx.findAssets(Query.builder().where('last_accessed < ').param(request.startDate).build(), [repo])
for (Asset item: assets) {
    tx.deleteAsset(item)
}

tx.commit()

def result = JsonOutput.toJson([
    assets  : "ok"
])
return result
