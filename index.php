<?php 
    	$url = "http://svcs.eBay.com/services/search/FindingService/v1?siteid=0&SECURITY-APPNAME=xiweiliu-c66b-45c6-bfc2-dd3e100fbfcf&OPERATION-NAME=findItemsAdvanced&SERVICE-VERSION=1.0.0&RESPONSE-DATA-FORMAT=XML";
    	$url = $url."&keywords=".$_GET["keywords"];
   		$url = $url."&sortOrder=".urlencode($_GET["sort"]);
   		$url = $url."&paginationInput.entriesPerPage=".urlencode($_GET["perPage"]); 
   		$url = $url."&paginationInput.pageNumber=".urlencode($_GET["curPageNum"]);
   		$counter = 0;   	
    	if($_GET["pricefrom"] != "") {
    		$url = $url."&itemFilter($counter).name=MinPrice&itemFilter($counter).value=".$_GET["pricefrom"];
   			$counter++;
   		}
   		if($_GET["priceto"] != "") {
   			$url = $url."&itemFilter($counter).name=MaxPrice&itemFilter($counter).value=".$_GET["priceto"];
    		$counter++;    			
    	}
    	$url = $url."&outputSelector[0]=SellerInfo&outputSelector[1]=PictureURLSuperSize&outputSelector[2]=StoreInfo";
//    	echo "<p>".$url."</p>";

    	$xml = simplexml_load_file($url) or die("Error: Cannot create object");
   		if($xml == null) {
   			$result = array("ack" => "oops");
   			echo json_encode($result);
   		} else if($xml->ack == "Success"){
    		if($xml->paginationOutput->totalEntries == 0) {
    			$result = array("ack" => "No results found");
    			echo json_encode($result);  // echo "No results found";
    		} else {
    			XML2JSON($xml);
    		}
    	} else {
    		$error = array("ack" => "No results found");
    		echo json_encode($error);  //	echo "something wrong";
    	}
    	
    	function XML2JSON($xml){
    		$result = array("ack" => (string)$xml->ack, "resultCount" => (string)$xml->paginationOutput->totalEntries, "pageNumber" => (string)$xml->paginationOutput->pageNumber, "itemCount" => (string)$xml->paginationOutput->entriesPerPage);
    		$itemNum = (int)$xml->searchResult[0]['count'];
    		for($i = 0; $i < $itemNum; $i++) {
    			$basicInfo = basic($xml, $i);
    			$sellerInfo= seller($xml, $i);
    			$shippingInfo= shipping($xml, $i);
    			$info = array("basicInfo" => $basicInfo, "sellerInfo" => $sellerInfo, "shippingInfo" => $shippingInfo);
    			$item = array("item".$i => $info);
    			$result = array_merge($result,$item);
    		}
    		echo json_encode($result);
    	}
    	function basic($xml, $i) {
    		$basic = array(
				"title" => (string)$xml->searchResult->item[$i]->title,
    			"viewItemURL" => (string)$xml->searchResult->item[$i]->viewItemURL,
    			"gallerURL" => (string)$xml->searchResult->item[$i]->galleryURL,
    			"pictureURLSuperSize" => (string)$xml->searchResult->item[$i]->pictureURLSuperSize,
    			"convertedCurrentPrice" => (string)$xml->searchResult->item[$i]->sellingStatus->convertedCurrentPrice,
    			"shippingServiceCost" => (string)$xml->searchResult->item[$i]->shippingInfo->shippingServiceCost,
    			"conditionDisplayName" => (string)$xml->searchResult->item[$i]->condition->conditionDisplayName,
    			"listingType" => (string)$xml->searchResult->item[$i]->listingInfo->listingType,
    			"location" => (string)$xml->searchResult->item[$i]->location,
    			"categoryName" => (string)$xml->searchResult->item[$i]->primaryCategory->categoryName,
    			"topRatedListing" => (string)$xml->searchResult->item[$i]->topRatedListing   				
    		);
    		return $basic;
    	}
    	function seller($xml, $i) {
    		$seller = array(
    			"sellerUserName" => (string)$xml->searchResult->item[$i]->sellerInfo->sellerUserName,
    			"feedbackScore" => (string)$xml->searchResult->item[$i]->sellerInfo->feedbackScore,
    			"positiveFeedbackPercent" => (string)$xml->searchResult->item[$i]->sellerInfo->positiveFeedbackPercent,
    			"feedbackRatingStar" => (string)$xml->searchResult->item[$i]->sellerInfo->feedbackRatingStar,
    			"topRatedSeller" => (string)$xml->searchResult->item[$i]->sellerInfo->topRatedSeller,
    			"sellerStoreName" => (string)$xml->searchResult->item[$i]->storeInfo->storeName,
    			"sellerStoreURL" => (string)$xml->searchResult->item[$i]->storeInfo->storeURL,	
    		);
    		return $seller;
    	}
    	function shipping($xml, $i) {
    		$toLocation = "";
    		foreach($xml->searchResult->item[$i]->shippingInfo->shipToLocations as $location){
    			$toLocation .= (string)$location.", ";
    		}
    		$toLocation = substr($toLocation, 0, -2);
    		$shipping = array(
    			"shippingType" => (string)$xml->searchResult->item[$i]->shippingInfo->shippingType,
    			"shipToLocations" => $toLocation,
    			"expeditedShipping" => (string)$xml->searchResult->item[$i]->shippingInfo->expeditedShipping,
    			"oneDayShippingAvailable" => (string)$xml->searchResult->item[$i]->shippingInfo->oneDayShippingAvailable,
    			"returnsAccepted" => (string)$xml->searchResult->item[$i]->returnsAccepted,
    			"handlingTime" => (string)$xml->searchResult->item[$i]->shippingInfo->handlingTime
    		);
    		return $shipping;
    	}

?>