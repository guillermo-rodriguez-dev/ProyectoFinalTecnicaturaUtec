// center of the map
var map
var center = [ -34.2999992, -57.2333298 ];
var shape_for_db
var poligonPredio
var formaPredio
var formaPotreros
var formaZona
var poligonPotrero
var poligonosZonas
var polygonoPredio
var turfPredio
var areaMinima = 1;
var area
var stringPotrerosJson
var potreros
var poligonZona
var idjs= 0;
var osmUrl = 'http://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png';
var osmAttrib = '&copy; <a href="http://openstreetmap.org/copyright">OpenStreetMap</a> contributors';
var osm = L.tileLayer(osmUrl, { maxZoom: 18, attribution: osmAttrib });

function crearMapaEditableConPolygones(idmap) {
	inizialisarMap(idmap, true)

	dibujarMapa()
	dibujarPredio();
	dibujarPotreros();
	dibujarZonas()
}

function crearMapaModificarPotreros(idmap) {
	inizialisarMap(idmap, true)

	dibujarMapaPotreros()
	dibujarPredio();
	dibujarPotreros();
	//dibujarZonas();
}
function crearMapaModificarZonas(idmap) {
	inizialisarMap(idmap, true)

	dibujarMapaZonas()
	dibujarPredio();
	//dibujarPotreros();
	dibujarZonas();
}

function crearMapaVacio(idmap) {
	inizialisarMap(idmap, true)

	dibujarMapa()
	if (map.hasLayer(poligonPredio)) {
		map.removeLayer(poligonPredio)
	}

	map.setView(center, 15);
}
function crearMapaNoModificable(idmap) {
	inizialisarMap(idmap, false)
	dibujarMapa()
	dibujarPredio()
	dibujarPotreros()
	dibujarZonas()

}
function dibujarPotreros() {
	if (map.hasLayer(poligonPotrero)) {
		map.removeLayer(poligonPotrero)
	}
	stringPotrerosJson = PF('formaPotrerosInput').jq.val();
	potreros = JSON.parse(stringPotrerosJson.replace(/\bNaN\b/g, "null"))
	// var forma;
	for ( var i in potreros) {
		 console.log(potreros[i]['indicadorActual']['color'])
		 		console.log(potreros[i])

		forma = potreros[i]['forma']['coordinates']['0']
		// console.log(forma)
		var formaArray = new Array();
		for (var y = 0; y < forma.length; y++) {
			formaArray.push([ forma[y][0], forma[y][1] ])
		}
		poligonPotrero = L.polygon(formaArray)

		poligonPotrero = L.polygon(formaArray, {

			className : 'label',

			color : potreros[i]['indicadorActual']['color']
		})
		poligonPotrero.bindTooltip(potreros[i]['nombre']+' - Indicador: '+potreros[i]['indicadorActual']['nombre']+ ' - \n Area:' + potreros[i]['areaEnHectareas'] + ' ha', {
			permanent : false,
			className : "my-label",
			offset : [ 0, 0 ]
		})
		poligonPotrero.addTo(formaPotreros)
	 poligonPotrero.idLayer = potreros[i]['id']
	}

}

function dibujarZonas() {
	if (map.hasLayer(poligonosZonas)) {
		map.removeLayer(poligonosZonas)
	}
	var stringZonasJson = PF('formaZonasInput').jq.val();
	console.log(stringZonasJson)
	var zonas = JSON.parse(stringZonasJson.replace(/\bNaN\b/g, "null"))
	console.log(zonas)
	for ( var i in zonas) {
		// console.log(zonas[i]['indicadorActual']['color'])
		forma = zonas[i]['forma']['coordinates']['0']
		console.log(forma)
		color= zonas[i]['tipoZona']['color'];
		console.log(color)
		var formaArray = new Array();
		for (var y = 0; y < forma.length; y++) {
			formaArray.push([ forma[y][0], forma[y][1] ])
		}
		poligonZona = L.polygon(formaArray,{
			color : '#' + color,

		})
		poligonZona.addTo(formaZona)
		poligonZona.idLayer = zonas[i]['id']

		
		console.log(zonas);
		
		poligonZona.bindTooltip(zonas[i]['nombre'] + '- Tipo de Zona:' + zonas[i]['tipoZona']['nombre'], + '-Area: ' + zonas[i]['areaEnHectareas'], {
			permanent : false,
			className : "my-label",
			

			offset : [ 0, 0 ]
		})
	}

}

function dibujarPredio() {
	if (map.hasLayer(poligonPredio)) {
		map.removeLayer(poligonPredio)
	}
	var val = PF('formaInput').jq.val();
	var forma = JSON.parse(val.replace(/\bNaN\b/g, "null"))// no tolera nan
	var formaArray = new Array();
	for (var i = 0; i < forma.length; i++) {
		formaArray.push([ forma[i].x, forma[i].y ])
	}
	map.setView([ forma[0].x, forma[0].y ]);
	polygonoPredio = L.polygon(formaArray)
	polygonoPredio.addTo(formaPredio)

	// console.log(forma)
	turfPredio = turf.polygon([ formaArray ], {
		name : 'predio'
	})
	
	bounds = formaPredio.getBounds();
    map.fitBounds(bounds);
}

function dibujarMapa() {
	   
	      // enable polygon drawing mode
	    
	formaPredio.pm.enable({
		  allowSelfIntersection: false,
		});
	

	map.on('pm:create', e => {

		var type = e.layerType;
		var layer = e.layer;
		formaPredio.addLayer(layer, {
			pmIgnore : false
		});
		var shape = layer.toGeoJSON()
		shape_for_db = JSON.stringify(shape.geometry);
		PF('formaInput').jq.val(shape_for_db);
		var input = document.getElementById('crearPredioForm:formaInput');
		input.value = shape_for_db;
	});
	formaPredio.on('pm:update',  e => {
		// var layer = formaPredio.layer;
		// formaPredio.addLayer(layer);
		var shape
		formaPredio.eachLayer(function(layer) {
			if (layer instanceof L.Polygon) {
				shape = layer.toGeoJSON()
			}
		})
		 console.log(e+ ' qawqeqwe');

		shape_for_db = JSON.stringify(shape.geometry);
		PF('formaInput').jq.val(shape_for_db);
		var input = document.getElementById('modificarPredio:formaInput');
		input.value = shape_for_db;
		// console.log(PF('formaInput').jq.val());
	});
}

function dibujarMapaPotreros() {


	formaPotreros.pm.enable({
		  allowSelfIntersection: false,
		});

	map.on('pm:create', e => {
		// agregarPotrero();
		console.log(e)

		var type = e.layerType;
		var layer = e.layer;
        map.removeLayer(layer);

		var turfNuevaLayer = turf.polygon(getArrayCoordenadas(layer))
		console.log('hola');

		// console.log(turfNuevaLayer);
		// console.log(turfPredio);

		var turfResultado = turf.intersect(turfPredio.geometry,
				turfNuevaLayer.geometry);
		area = turf.area(turfResultado) / 10000;

		if (turfResultado == null || area < areaMinima) {
			console.log(area);
		}

		else {

			formaPotreros
					.eachLayer(function(layer) {
						if (layer instanceof L.Polygon
								|| layer instanceof L.MultiPolygon) {
							var turfLayerActual = turf
									.polygon(getArrayCoordenadas(layer));
							turfResta = turf.difference(turfResultado,
									turfLayerActual);

							if (turfResta == null) {
								turfResultado == null
							} else {
								turfResultado = turfResta;
							}
						}
					})
			if (turfResultado == null) {

			} else {
				area = turf.area(turfResultado) / 10000;
				marker = L.polygon(turf.getCoords(turfResultado), {
					pmIgnore : false
				}).addTo(map);
				formaPotreros.addLayer(marker, {
					pmIgnore : false
				});

				var shape = marker.toGeoJSON()
				shape_for_db = JSON.stringify(shape);
				idjs = idjs - 1;
				marker.idLayer=idjs;
				agregarPotrero([ {
					name : 'potrero',
					value : shape_for_db
				},{
					name : 'id',
					value : idjs
				}  ]);

				console.log(shape_for_db);
				PF('formaInput').jq.val(shape_for_db);
				var input = document
						.getElementById('crearPredioForm:formaInput');
				input.value = shape_for_db;
			}

		}
	});
	formaPotreros.on('pm:update',  e => {
		console.log('updateee')
				console.log(e)

		var type = e.layerType;
		var layer = e.sourceTarget;
		var id = layer.idLayer
		var color = layer.options.color;
        map.removeLayer(layer);

		var turfNuevaLayer = turf.polygon(getArrayCoordenadas(layer))
		console.log('hola');
		var turfResultado = turf.intersect(turfPredio.geometry,
				turfNuevaLayer.geometry);
		area = turf.area(turfResultado) / 10000;

		if (turfResultado == null || area < areaMinima) {
			console.log(area);
		}
		else {

			formaPotreros
					.eachLayer(function(layer) {
						if (layer instanceof L.Polygon
								|| layer instanceof L.MultiPolygon) {
							var turfLayerActual = turf
									.polygon(getArrayCoordenadas(layer));
							turfResta = turf.difference(turfResultado,
									turfLayerActual);

							if (turfResta == null) {
								turfResultado == null
							} else {
								turfResultado = turfResta;
							}
						}
					})
			if (turfResultado == null) {

			} else {
				area = turf.area(turfResultado) / 10000;
				marker = L.polygon(turf.getCoords(turfResultado), {
					pmIgnore : false,
color: color
				}).addTo(map);
				formaPotreros.addLayer(marker, {
					pmIgnore : false
				});

				var shape = marker.toGeoJSON()
				shape_for_db = JSON.stringify(shape);
				modificarPotrero([ {
					name : 'potrero',
					value : shape_for_db
				},{
					name : 'id',
					value: id 
				} ]);
				marker.idLayer = id;
				console.log(marker.idLayer);
				PF('formaInput').jq.val(shape_for_db);
				var input = document
						.getElementById('crearPredioForm:formaInput');
				input.value = shape_for_db;
			}
		}
	});
}
function dibujarMapaZonas() {
	formaZona.pm.enable({
		  allowSelfIntersection: false,
		});
	map.on('pm:create', e => {
		var type = e.layerType;
		var layer = e.layer;
        map.removeLayer(layer);
		var turfNuevaLayer = turf.polygon(getArrayCoordenadas(layer))
		console.log('hola');
		var turfResultado = turf.intersect(turfPredio.geometry,
				turfNuevaLayer.geometry);
		area = turf.area(turfResultado) / 10000;

		if (turfResultado == null || area < areaMinima) {
			console.log(area);
		}
		else {
			formaZona
					.eachLayer(function(layer) {
						//console.log(poligonZona.idLayer + '1qwee12e')
						if (layer instanceof L.Polygon
								|| layer instanceof L.MultiPolygon) {
							var turfLayerActual = turf
									.polygon(getArrayCoordenadas(layer));
							turfResta = turf.difference(turfResultado,
									turfLayerActual);

							if (turfResta == null) {
								turfResultado == null
							} else {
								turfResultado = turfResta;
							}
						}
					})
			if (turfResultado == null) {

			} else {
				area = turf.area(turfResultado) / 10000;
				marker = L.polygon(turf.getCoords(turfResultado), {
					pmIgnore : false
				}).addTo(map);
				formaZona.addLayer(marker, {
					pmIgnore : false
				});

				var shape = marker.toGeoJSON()
				shape_for_db = JSON.stringify(shape);
				 idjs = idjs-1;

				 agregarZona([ {
					 name : 'zona',
					 value : shape_for_db,
				 }, {
					 name : 'id',
					 value : idjs,
				 }]);
				 marker.idLayer = idjs
				console.log(marker.idLayer);
				PF('formaInput').jq.val(shape_for_db);
				var input = document
						.getElementById('modificarZonasForm:formaInput');
				input.value = shape_for_db;
			}

		}
	});
	formaZona.on('pm:update',  e => {
		console.log('updateee')
				console.log(e)

		var type = e.layerType;
		var layer = e.sourceTarget;
		var id = layer.idLayer
		var color = layer.options.color;
        map.removeLayer(layer);

		var turfNuevaLayer = turf.polygon(getArrayCoordenadas(layer))
		console.log('hola');
		var turfResultado = turf.intersect(turfPredio.geometry,
				turfNuevaLayer.geometry);
		area = turf.area(turfResultado) / 10000;

		if (turfResultado == null || area < areaMinima) {
			console.log(area);
		}
		else {

			formaZona
					.eachLayer(function(layer) {
						if (layer instanceof L.Polygon
								|| layer instanceof L.MultiPolygon) {
							var turfLayerActual = turf
									.polygon(getArrayCoordenadas(layer));
							turfResta = turf.difference(turfResultado,
									turfLayerActual);

							if (turfResta == null) {
								turfResultado == null
							} else {
								turfResultado = turfResta;
							}
						}
					})
			if (turfResultado == null) {

			} else {
				area = turf.area(turfResultado) / 10000;
				marker = L.polygon(turf.getCoords(turfResultado), {
					pmIgnore : false,
color: color
				}).addTo(map);
				formaZona.addLayer(marker, {
					pmIgnore : false
				});

				var shape = marker.toGeoJSON()
				shape_for_db = JSON.stringify(shape);
				modificarZona([ {
					name : 'zona',
					value : shape_for_db
				},{
					name : 'id',
					value: id 
				} ]);
				marker.idLayer = id;
				console.log(marker.idLayer);
				PF('formaInput').jq.val(shape_for_db);
				var input = document
						.getElementById('modificarZonasForm:formaInput');
				input.value = shape_for_db;
			}
		}
	});
}
function getArrayCoordenadas(layer) {
	latlngs = layer.getLatLngs();
	poligonos = new Array(latlngs.length);
	for (i = 0; i < latlngs.length; i++) {
		ll = latlngs[i];
		poligonos[i] = new Array(ll.length);
		strCoords = "[";
		for (j = 0; j < ll.length; j++) {
			lat = ll[j].lat;
			lng = ll[j].lng;
			poligonos[i][j] = new Array(2);
			poligonos[i][j][0] = Number(lat);
			poligonos[i][j][1] = Number(lng);
			strCoords = strCoords + Number(lat) + " " + Number(lng)
		}
		ultimo = poligonos[i].length - 1;
		primerLat = poligonos[i][0][0];
		primerLng = poligonos[i][0][1];
		ultimaLat = poligonos[i][ultimo][0];
		ultimaLng = poligonos[i][ultimo][1]

		if (primerLat != ultimaLat || primerLng != ultimaLng) {
			// si las últimas y primeras coordenadas no son las mismas
			poligonos[i][ultimo + 1] = Array(2);
			poligonos[i][ultimo + 1][0] = primerLat;
			poligonos[i][ultimo + 1][1] = primerLng;
		}
		strCoords = strCoords + "]";
	}
	return poligonos;
}
function inizialisarMap(idmap, isEditable, layerZonas, layerPotreros, LayerPredio) {
	console.log(map);
	if (map != undefined || map != null) {
		map.off();
		map.remove();
	}
	map = new L.Map(idmap, { center: new L.LatLng(-32.241, -55.838), zoom: 8 });
	formaPredio = new L.FeatureGroup({
	}).addTo(map);
	formaPotreros = new L.FeatureGroup({
	}).addTo(map);
	formaZona = new L.FeatureGroup({
	}).addTo(map);
	L.control.layers({
		"google": L.tileLayer('http://www.google.cn/maps/vt?lyrs=s@189&gl=cn&x={x}&y={y}&z={z}', {
		    attribution: 'google'
		}
		).addTo(map)},
		{
			'Forma Predio': formaPredio,
			'Zonas Geograficas': formaZona,
		    'Potreros': formaPotreros,
		}, { position: 'topleft', collapsed: true }).addTo(map);
	    map.pm.setLang('es');		
	    map.pm.addControls({
	        position: 'topleft',
	        drawCircle: false,
	        drawMarker: false,
	        drawCircleMarker: false,
	        drawPolyline: false,
	        drawRectangle: false,
	        drawPolygon: isEditable,
	        drawCircle: false,
	        editMode: isEditable,
	        dragMode: false,    // para arrastrar poligono
	     // cutPolygon: true,
	        removalMode:false,
	        pinningOption: false,
	        snappingOption: false,
	        allowSelfIntersection: false,
	    });
	  
	if(isEditable){
	  map.pm.enableDraw('Polygon', {
	        snappable: true,
	        snapDistance: 30,
	        allowSelfIntersection: false,
	      });
	  
	  formaPredio.pm.enable({
		  allowSelfIntersection: false,
		});
	}

}
function removerZona(id){
	console.log(id + 'enviado')
	formaZona
	.eachLayer(function(layer) {
		console.log(layer.idLayer + 'tenido')
		if(layer.idLayer == id){
			formaZona.removeLayer(layer);
		}
	})
}
function removerPotrero(id){
	console.log(id + 'enviado')
	formaPotreros
	.eachLayer(function(layer) {
		console.log(layer.idLayer + 'tenido')
		if(layer.idLayer == id){
			formaPotreros.removeLayer(layer);
		}
	})
	

}

function cambiarColorPotrero(id, colorNuevo){
	console.log(id + 'enviado')
	formaPotreros
	.eachLayer(function(layer) {
		console.log(layer.idLayer + 'tenido')
		if(layer.idLayer == id){
			formaPotreros.setStyle({color:colorNuevo});
		}
	})
	

}
function resetearFormulario(idFormulario){
	document.getElementById(idFormulario).reset();

}




