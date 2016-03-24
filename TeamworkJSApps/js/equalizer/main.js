/**
*
* Loop Waveform Visualizer by Felix Turner
* www.airtight.cc
*
* Audio Reactive Waveform via Web Audio API.
*
*/
var app = app || {};

app.equalizer = (function() {
	
	var mouseX = 0, mouseY = 0, windowHalfX = window.innerWidth / 2, windowHalfY = window.innerHeight / 2, camera, scene, renderer, material, container;
	var source;
	var analyser;
	var buffer;
	var audioBuffer;
	var dropArea;
	var audioContext;
	var source;
	var xhr;
	var started = false;

	$(document).ready(function() {

		//Chrome is only browser to currently support Web Audio API
		var is_chrome = navigator.userAgent.toLowerCase().indexOf('chrome') > -1;
		var canvas = document.createElement('canvas');
		canvas.style.width = "600px";
		canvas.style.height = "400px";
		canvas.style.borderRadius = "50";
		var is_webgl = ( function () { try { return !! window.WebGLRenderingContext && !! canvas.getContext( 'experimental-webgl' ); } catch( e ) { return false; } } )();

		if(!is_chrome){
			$('#loading').html("This feature is only supported in Google Chrome");
		} else if(!is_webgl){
			$('#loading').html('Your graphics card does not seem to support <a href="http://khronos.org/webgl/wiki/Getting_a_WebGL_Implementation">WebGL</a>.<br />' +
			'Find out how to get it <a href="http://get.webgl.org/">here</a>, or try restarting your browser.');
		}else {
			$('#loading').html();
		}
	});

	function init(url) {
		//init 3D scene
		var container = document.getElementById('equalizer');
		camera = new THREE.PerspectiveCamera(60, window.innerWidth / window.innerHeight, 1, 1000000);
		camera.position.z = 2500;
		scene = new THREE.Scene();
		scene.add(camera);
		renderer = new THREE.WebGLRenderer({
			antialias : false,
			sortObjects : false
		});
		renderer.setSize(600, 400);

		container.appendChild(renderer.domElement);

		// stop the user getting a text cursor
		document.onselectStart = function() {
			return false;
		};

	//	//add stats
	//	stats = new Stats();
	//	stats.domElement.style.position = 'absolute';
	//	stats.domElement.style.top = '0px';
	//	container.appendChild(stats.domElement);

		//init listeners
		$(document).mousemove(onDocumentMouseMove);
		$(window).resize(onWindowResize);
		startEverything(url);
		
		onWindowResize(null);

	}

	function loadAudioBuffer(url) {

		// Load asynchronously
		var request = new XMLHttpRequest();
		request.open("GET", url, true);
		request.responseType = "arraybuffer";

		request.onload = function() {


			audioContext.decodeAudioData(request.response, function(buffer) {
				audioBuffer = buffer;
				finishLoad();
			}, function(e) {
				console.log(e);
			});


		};
		request.send();

	}

	function finishLoad() {
		source.buffer = audioBuffer;
		source.loop = true;
		source.start(0.0);
		startViz();
	}

	function onDocumentMouseMove(event) {	
		mouseX = (event.clientX - windowHalfX);
		mouseY = (event.clientY - windowHalfY);
	}

	function onWindowResize(event) {
		windowHalfX = window.innerWidth / 2;
		windowHalfY = window.innerHeight / 2;
		camera.aspect = window.innerWidth / window.innerHeight;
		camera.updateProjectionMatrix();
		renderer.setSize(800, 400);
	}

	function animate() {
		requestAnimationFrame(animate);
		render();
	}

	function render() {
		LoopVisualizer.update(analyser);

		var xrot = mouseX/window.innerWidth * Math.PI*2 + Math.PI;
		var yrot = mouseY/window.innerHeight* Math.PI*2 + Math.PI;

		LoopVisualizer.loopHolder.rotation.x += (-yrot - LoopVisualizer.loopHolder.rotation.x) * 0.3;
		LoopVisualizer.loopHolder.rotation.y += (xrot - LoopVisualizer.loopHolder.rotation.y) * 0.3;

		renderer.render(scene, camera);
	}

	$(window).mousewheel(function(event, delta) {
		//set camera Z
		camera.position.z -= delta * 50;
	});

	function saveFile(url) {
		// Get file name from url.
		var filename = url.substring(url.lastIndexOf("/") + 1).split("?")[0];
		var xhr = new XMLHttpRequest();
		xhr.responseType = 'blob';
		xhr.onload = function() {
			var a = document.createElement('a');
			var currentFile = xhr.response;
			playMe(currentFile);
		 // xhr.response is a blob
			delete a;
		};
		xhr.open('GET', url);
		xhr.send();
	}


	function startEverything(url){
		saveFile(url);
	}


	function playMe(currentFile) {
		//clean up previous mp3
		if (source) source.disconnect();
		LoopVisualizer.remove();

		$('#loading').show();
		$('#loading').text("loading...");

		console.log(currentFile);

		var reader = new FileReader();

		reader.onload = function(fileEvent) {
			var data = fileEvent.target.result;

			var encodedString = String.fromCharCode.apply(null, data),
			decodedString = decodeURIComponent(escape(atob(encodedString)));

			console.log(decodedString);

			initAudio(data);
		};

		reader.readAsArrayBuffer(currentFile);

	}

	function initAudio(data) {
		source = audioContext.createBufferSource();

		if(audioContext.decodeAudioData) {
			audioContext.decodeAudioData(data, function(buffer) {
				source.buffer = buffer;
				createAudio();
			}, function(e) {
				console.log(e);
				$('#loading').text("cannot decode mp3");
			});
		} else {
			source.buffer = audioContext.createBuffer(data, false );
			createAudio();
		}
	}


	function createAudio() {

		analyser = audioContext.createAnalyser();
		analyser.smoothingTimeConstant = 0.1;
		analyser.fftSize = 1024;

		source.connect(audioContext.destination);
		source.connect(analyser);
		source.start(0);
		source.loop = false;

		startViz(analyser);
	}

	function startViz(analyser){

		$('#loading').hide();

		LoopVisualizer.init(analyser, scene);

		if (!started){
			started = true;
			animate();
		}

	}
	
	return {
		init: init,
		audioContext: function(){
			if(!audioContext){
				audioContext = new AudioContext();
			}
			return audioContext;
		}(),
	}
})();