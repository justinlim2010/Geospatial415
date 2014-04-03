/**
 * Javacript source for Need For Speed Application
 */

/* Global variables */
var baseMapLayerURL = 'http://{s}.tile.cloudmade.com/{key}/{styleId}/256/{z}/{x}/{y}.png';
var baseMapLayerAttribution = 'Map data &copy; <a href="http://openstreetmap.org">OpenStreetMap</a> contributors, <a href="http://creativecommons.org/licenses/by-sa/2.0/">CC-BY-SA</a>, Imagery © <a href="http://cloudmade.com">CloudMade</a>';
var overlay = new Array();
var baseMaps, overlayMaps;
var controlLayer, sidebar;
var map;

/* Run initialize function when HTML document is loaded */
window.onload = initialize();

/* This function will call required function to display the map and all the layers */
function initialize()
{
  // Set base map
  setBaseMap();
  
  // Set map sidebar
  setSidebar();
  
  // Get real-time data feed from LTA API
  getRoadIncidentData();
  
  // Get road section data and add to overlayMaps
//  var roadSectionFileLocation = "data/RoadSectionLine.geojson";
//  var roadSectionStyle = function( feature ) {
//	  return {
//		  color : "#808080",
//		  weight : 1,
//		  opacity : 1
//		  };
//  };
//  var RoadSectionFeature = function( feature, layer ) {};
//  var roadSectionLayerName = "Road Section";
//  setOverlayMapFile(roadSectionFileLocation, roadSectionStyle, RoadSectionFeature, roadSectionLayerName);
  
  // Get ERP Gantry data and add to overlayMaps
  var ERPGantryFileLocation = "data/ERPGantryPoint.geojson";
  var ERPGantryStyle = function( feature ) {
	  return {
		  color : "#0000FF",
		  weight : 5,
		  opacity : 1
		  };
  };
  var ERPGantryFeature = function( feature, layer ) {
	  layer.on( 'mouseover', function( e ){
		  e.target.bindPopup( feature.properties.description ).openPopup();
	  } );
  };
  var ERPGantryLayerName = "ERP Gantry";
  setOverlayMapFile(ERPGantryFileLocation, ERPGantryStyle, ERPGantryFeature, ERPGantryLayerName);
};

/* This function will set the base map */
function setBaseMap()
{
  var baseMapLayer = L.tileLayer( baseMapLayerURL, {
    key : '8bc33f0d529540bbbc631f1ae3705abd',
    styleId : 998,
    attribution : baseMapLayerAttribution
  } );

  var midnight = L.tileLayer( baseMapLayerURL, {
    key : '8bc33f0d529540bbbc631f1ae3705abd',
    styleId : 999,
    attribution : baseMapLayerAttribution
  } );

  var chrono = L.tileLayer( baseMapLayerURL, {
    key : '8bc33f0d529540bbbc631f1ae3705abd',
    styleId : 22677,
    attribution : baseMapLayerAttribution
  } );

  // Setting map with default baseMapLayer and it's coordinate
  map = L.map( 'map', {
    center : new L.LatLng( 1.355312, 103.827068 ),
    zoom : 11,
    layers : [ baseMapLayer ]
  } );
  
  // Display loading notification
  map.spin( true );
  
  //Layergroup control for basemap
  baseMaps = {
    "Basemap" : baseMapLayer,
    "Chronomap" : chrono,
    "Night View" : midnight
  };
  
  //Layergroup control for basemap
  overlayMaps = {};
  
  controlLayer = L.control.layers( baseMaps, overlayMaps );
  controlLayer.addTo( map );
};

/* This function will set the overlay map from file */
function setOverlayMapFile(fileLoc, layerStyle, layerFeature, layerName) {
	// Display loading notification
	map.spin( true );
	
	var overlay = L.geoJson( null, {
	  style : layerStyle,
	  onEachFeature : layerFeature
	} );
	
	// Add GeoJSON data
	$.getJSON( fileLoc, function( data )
	{
		overlay.addData( data );
		overlay.addTo( map );
		console.log( data );
		
		// Add layer to layer control
		overlayMaps[layerName] = overlay;
		
		// Remove and update control layer
		controlLayer.removeFrom( map );
		controlLayer = L.control.layers( baseMaps, overlayMaps );
		controlLayer.addTo( map );
	} );
}

/* This function will set the overlay map from variable */
function setOverlayMapVar(file, layerStyle, layerFeature, layerName) 
{
	// Display loading notification
	map.spin( true );
	
	var overlay = L.geoJson( null, {
	  style : layerStyle,
	  onEachFeature : layerFeature
	} );
	
	// Add GeoJSON data
	overlay.addData( file );
	overlay.addTo( map );
	console.log( file );
	
	// Add layer to layer control
	overlayMaps[layerName] = overlay;
	
	// Remove and update control layer
	controlLayer.removeFrom( map );
	controlLayer = L.control.layers( baseMaps, overlayMaps );
	controlLayer.addTo( map );
}

/* Function to get real-time data feed from LTA */
function getRoadIncidentData()
{
$.ajax( {
    url : "/LTA/TrafficIncidents",
    type : 'GET',
    dataType : 'json'
  } )
  .done( function( jsonArray )
  {
    var features = [];
    $.each( jsonArray, function( index, element )
    {
      $.each( element, function( index, element )
      {
        // Create coordinate data
        var coordinate = [ parseFloat(element.long), parseFloat(element.lat) ];

        // Create the geometry data
        var geometry = {
          "type" : "Point",
          "coordinates" : coordinate
        };

        // Create the properties data
        var properties = {
          "incidentId" : element.incidentId,
          "incidentType" : element.type,
          "description" : element.description
        };

        // Create the jsonObject
        var jsonObj = {
          "type" : "Feature",
          "geometry" : geometry,
          "properties" : properties
        };

        // Push to JSONList
        features.push( jsonObj );
      } );
    } );

    // Create GeoJSON Object
    var geoJson = {
      "type" : "FeatureCollection",
      "features" : features
    };

    console.log( geoJson );

    // Create layer
    var incidentLayer = L.geoJson( geoJson, {
      onEachFeature : function( feature, layer )
      {
        layer.on( 'mouseover', function( e )
        {
          e.target.bindPopup( feature.properties.description ).openPopup();
        } );
      }
    } );

    // Set markercluster
    var incidentLayerCluster = new L.MarkerClusterGroup();
    incidentLayerCluster.addLayer( incidentLayer );
    map.addLayer( incidentLayerCluster );
    
    // Add layer to layer control
	overlayMaps["Incident"] = incidentLayerCluster;
    
    // Remove and update control layer
	controlLayer.removeFrom( map );
	controlLayer = L.control.layers( baseMaps, overlayMaps );
	controlLayer.addTo( map );
  } )
  .fail( function( data )
  {
    console.log( data );
  } );
}

/* Function to set sidebar to map layer */
function setSidebar()
{
	sidebar = L.control.sidebar("sidebar", {
	  	  closeButton: true,
	  	  position: "left"
	  	}).addTo(map);
}

/* Function to convert SHPfile to GeoJSON format */
function uploadSHP()
{
	// Set opencpu url
	ocpu.seturl( "http://localhost:8081/ocpu/library/needForSpeed/R" );
	
	// Set file and header
	var shpfile = $("#shpfile")[0].files[0];
	var shpprojection = $("#shpprojection").val();
	var layername = $("#shplayername").val();
	
	// Convert SHP file to GeoJSON
	var req = ocpu.rpc( "togeojson", {
		file : shpfile,
		projection : shpprojection
		}, function( session )
		{
			var output = JSON.parse(session);
			console.log( output );
			
			// Apply GeoJSON to layer
			var jsonStyle = function( feature ) {};
			var jsonFeature = function( feature, layer ) {};
			var jsonName = layername;
			setOverlayMapVar(output, jsonStyle, jsonFeature, jsonName);
		} );
	
	// (Optional) Display alert if upload function fail
	req.fail( function()
	{
		alert( "Sorry, we encountered an error while processing the data. Please re-upload the file." );
	} );
}

/* Call kde function from needForSpeed R package */
function calculateKDE()
{
	// Set opencpu url
	ocpu.seturl( "http://localhost:8081/ocpu/library/needForSpeed/R" );
	
	// Set file and header
	var shpfile = $("#shpfile")[0].files[0];
	
	// Convert SHP file to GeoJSON
	var req = $('#chart').rplot( "plotChart", {
			file : shpfile
		});
	
	// (Optional) Display alert if upload function fail
	req.fail( function()
	{
		alert( "Sorry, we encountered an error while processing the data. Please re-upload the file." );
	} );
}

/* Call uploadSHP function when submit button is clicked */
$("#submit-shp").on("click", function() 
{
	//uploadSHP();
	calculateKDE();
});

/* Transform GeoJSON projection when submit button is clicked */
$("#submit-json").on("click", function() 
{
	// Set opencpu url
	ocpu.seturl( "http://localhost:8081/ocpu/library/needForSpeed/R" );
	
	// Set file and header
	var jsonfile = $("#jsonfile")[0].files[0];
	var jsonprojection = $("#jsonprojection").val();
	var layername = $("#jsonlayername").val();
	
	var reader = new FileReader();
	reader.readAsText(jsonfile);

	reader.onload = function(e) {
		var content = reader.result;
		
		// Convert SHP file to GeoJSON
		var req = ocpu.rpc( "transformProjection", {
			text : content,
			projection : jsonprojection
			}, function( session )
			{
				var output = JSON.parse(session);
				
				// Apply GeoJSON to layer
				var jsonStyle = function( feature ) {};
				var jsonFeature = function( feature, layer ) {};
				var jsonName = layername;
				setOverlayMapVar(output, jsonStyle, jsonFeature, jsonName);
			} );
		
		// (Optional) Display alert if upload function fail
		req.fail( function()
		{
			alert( "Sorry, we encountered an error while processing the data. Please re-upload the file." );
		} );
	};
});

/* Hide loading notification when all AJAX request complete */
$( document ).ajaxComplete(function() {
	// Hide loading notification
	map.spin( false );
});

////set CORS to call "stocks" package on public server
//ocpu.seturl("http://localhost:8081/ocpu/library/needForSpeed/R")
//
////call R function: stocks::smoothplot(ticker=ticker)
//$(document).ready(function(){  
//    var req = $("#chart").rplot("smoothplot", {
//        ticker : "GOOG",
//        from : "2013-01-01"
//    });
//    
//    //optional
//    req.fail(function(){
//        alert("R returned an error: " + req.responseText); 
//    });
//});