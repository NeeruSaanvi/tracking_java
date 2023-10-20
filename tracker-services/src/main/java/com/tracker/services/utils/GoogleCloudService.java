package com.tracker.services.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Acl;
import com.google.cloud.storage.Acl.Role;
import com.google.cloud.storage.Acl.User;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.common.collect.Lists;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class GoogleCloudService {

	@Value("${google.credentials.file}")
	private String googleCredentialsFile;

	public URL getBucketImageUrl(String bucketName, String imageName) {
		
		GoogleCredentials credentials = getGoogleCredentials();
		URL signedUrl = null;

		
		if(credentials != null) {
		Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();

		Blob blob = storage.get(BlobId.of(bucketName, imageName));
		if (blob != null)
			signedUrl = storage.signUrl(BlobInfo.newBuilder(bucketName, blob.getName()).build(), 14, TimeUnit.DAYS);

		}
		return signedUrl;
	}
	
	public URL getMMSBucketImageUrl(String bucketName, byte[] inputSchema, String fileName) {
		
		deleteFilesFromBucket(bucketName);

		GoogleCredentials credentials = getGoogleCredentials();

		Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();

		storage.create(BlobInfo.newBuilder(bucketName, fileName).build(), inputSchema);

		Blob blob = storage.get(BlobId.of(bucketName, fileName));
		//blob. content_type = "image/jpeg";
				
		BlobInfo blobInfo =
			      storage.create(
			          BlobInfo
			              .newBuilder(bucketName, fileName)
			              .setContentType("image/jpeg")
			              // Modify access list to allow all users with link to read file
			              .setAcl(new ArrayList<>(Arrays.asList(Acl.of(User.ofAllUsers(), Role.OWNER))))
			              .build(),
			              inputSchema);
		
		//String iname = blobInfo.getMediaLink();
		
		URL signedUrl = null;
		if (blob != null)
			signedUrl = storage.signUrl(blobInfo, 14, TimeUnit.DAYS);

		log.info("signedUrl {} ", signedUrl);
		return signedUrl;
	}
	
	public String uploadFileToBucket(String bucketName, byte[] inputSchema, String fileName) {

		GoogleCredentials credentials = getGoogleCredentials();

		Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
		
		BlobId blobId = BlobId.of(bucketName, fileName);
//		BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("text/plain").build();
	    BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();
		Blob blob = storage.create(blobInfo, inputSchema);

		String signedUrl = null;
		if (blob != null) {
			storage.createAcl(blobId, Acl.of(Acl.User.ofAllUsers(), Acl.Role.READER));
			signedUrl = "https://storage.googleapis.com/"+blob.getBucket()+"/"+blob.getName();
			//signedUrl = storage.signUrl(BlobInfo.newBuilder(bucketName, blob.getName()).build(), 14);
		}

		log.info("signedUrl {} ", signedUrl);
		return signedUrl;
	}
	
	public void deleteFilesFromBucket(String bucketName) {
		GoogleCredentials credentials = getGoogleCredentials();

		Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
		Iterable<Blob> blobs = storage.list(bucketName).iterateAll();
		
		for (Blob blob : blobs) {
		    blob.delete(Blob.BlobSourceOption.generationMatch());
		}
	}

	private GoogleCredentials getGoogleCredentials() {
		GoogleCredentials credentials = null;
		try {
			credentials = GoogleCredentials.fromStream(new FileInputStream(googleCredentialsFile))
					.createScoped(Lists.newArrayList("https://www.googleapis.com/auth/cloud-platform"));
		} catch (IOException e) {
			e.printStackTrace();
			log.error("Invalid gogole cloud credential");
		}
		return credentials;
	}

}

	