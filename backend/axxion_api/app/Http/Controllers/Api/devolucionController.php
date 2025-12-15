<?php

namespace App\Http\Controllers\api;

use App\Http\Controllers\Controller;
use App\Models\Devolucion;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Validator;
use Illuminate\Validation\Rule;

class devolucionController extends Controller
    {
    /**
     * Lista todas las devoluciones registradas.
     */
        public function index(){
        $devoluciones = Devolucion::all();
        $data = [
            'devoluciones' => $devoluciones,
            'status' => 200
        ];
        return response()->json($data, 200);
        }
    /**
     * Registra una nueva devolución de renta.
     * 
     * ANALOGÍA: Esta función es como el proceso de 'Check-out' en un hotel o alquiler de autos. 
     * Se registra cuándo se devolvió el ítem, quién lo recibió y en qué estado se encuentra 
     * (si hay daños o faltantes).
     */
        public function store(Request $request){
            $validator = Validator::make($request->all(),[
                'renta_id' => [ 'required', Rule::exists('renta', 'id')],
                'fecha_devolucion_programada' => ['required', 'date'],
                'fecha_devolucion_real' => ['required', 'date'],
                'estado_devolucion' => [ 'string', Rule::in(['Pendiente','EnProcesoInspeccion','Completa','IncompletaConProblemas'])],
                'persona_recibe' => ['required', 'string'],
                'notas_generales' => ['required', 'string']
            ]);
            if($validator->fails()){
                $data = [
                    'message' => 'Validation failed',
                    'errors' => $validator->errors(),
                    'status' => 422
                ];
                return response()->json($data, 422);
            }
            $devolucion = Devolucion::create([
                'renta_id' => $request->renta_id,
                'fecha_devolucion_programada' => $request->fecha_devolucion_programada,
                'fecha_devolucion_real' => $request->fecha_devolucion_real,
                'estado_devolucion' => $request->estado_devolucion,
                'persona_recibe' => $request->persona_recibe,
                'notas_generales' => $request->notas_generales
            ]);
            if(!$devolucion){
                $data = [
                    'message' => 'Error al crear devolucion',
                    'status' => 500
                ];
                return response()->json($data, 500);
            }
            $devolucion->load('renta');
            $data = [
                'devolucion' => $devolucion,
                'status' => 201
            ];
            return response()->json($data, 201);
        }
    /**
     * Muestra los detalles de una devolución específica.
     */
        public function show($id){
            $devolucion = Devolucion::find($id);
            if(!$devolucion){
                $data = [
                    'message' => 'Devolucion no encontrada',
                    'status' => 404
                ];
                return response()->json($data, 404);
            }
            $data = [
                'devolucion' => $devolucion,
                'status' => 200
            ];
            return response()->json($data, 200);
        }
    /**
     * Elimina un registro de devolución.
     */
        public function destroy($id){
            $devolucion = Devolucion::find($id);
            if(!$devolucion){
                $data = [
                    'message' => 'Devolucion no encontrada',
                    'status' => 404
                ];
                return response()->json($data, 404);
            }
            $devolucion->delete();
            $data = [
                'devolucion' => $devolucion,
                'status' => 200
            ];
            return response()->json($data, 200);
        }
    /**
     * Actualiza la información de una devolución.
     */
        public function update(Request $request, $id){
            $devolucion = Devolucion::find($id);
            if(!$devolucion){
                $data = [
                    'message' => 'Devolucion no encontrada',
                    'status' => 404
                ];
                return response()->json($data, 404);
            }
            $validator = Validator::make($request->all(),[
                'renta_id' => [ 'required', Rule::exists('renta', 'id')],
                'fecha_devolucion_programada' => ['required', 'date'],
                'fecha_devolucion_real' => ['required', 'date'],
                'estado_devolucion' => ['required', 'string', Rule::in(['Pendiente','EnProcesoInspeccion','Completa','IncompletaConProblemas'])],
                'persona_recibe' => ['required', 'string'],
                'notas_generales' => ['required', 'string']
            ]);
            if($validator->fails()){
                $data = [
                    'message' => 'Validator failed',
                    'errors' => $validator->errors(),
                    'status' => 422
                ];
                return response()->json($data, 422);
            }
            $devolucion->renta_id = $request->renta_id;
            $devolucion->fecha_devolucion_programada = $request->fecha_devolucion_programada;
            $devolucion->fecha_devolucion_real = $request->fecha_devolucion_real;
            $devolucion->estado_devolucion = $request->estado_devolucion;
            $devolucion->persona_recibe = $request->persona_recibe;
            $devolucion->notas_generales = $request->notas_generales;
            $devolucion->save();
            $devolucion->load('renta');
            $data = [
                'message' => 'devolucion actualizado',
                'devolucion' => $devolucion,
                'status' => 200
            ];
            return response()->json($data, 200);
        }
    /**
     * Actualización parcial de una devolución.
     */
        public function updatePartial(Request $request, $id){
            $devolucion = Devolucion::find($id);
            if(!$devolucion){
                $data = [
                    'message' => 'devolucion no encontrada',
                    'status' => 404
                ];
                return response()->json($data, 404);
            }
            $validator = Validator::make($request->all(),[
                'renta_id' => [ Rule::exists('renta', 'id')],
                'fecha_devolucion_programada' => ['date'],
                'fecha_devolucion_real' => ['date'],
                'estado_devolucion' => ['string', Rule::in(['Pendiente','EnProcesoInspeccion','Completa','IncompletaConProblemas'])],
                'persona_recibe' => ['string'],
                'notas_generales' => ['string']
            ]);
            if($validator->fails()){
                $data = [
                    'message' => 'Error en la Validacion',
                    'errors' => $validator->errors(),
                    'status' => 422
                ];
                return response()->json($data, 422);
            }

            if($request->has('renta_id')){
                $devolucion->renta_id = $request->renta_id;
            }
            if($request->has('fecha_devolucion_programada')){
                $devolucion->fecha_devolucion_programada = $request->fecha_devolucion_programada;
            }
            if($request->has('fecha_devolucion_real')){
                $devolucion->fecha_devolucion_real = $request->fecha_devolucion_real;
            }
            if($request->has('estado_devolucion')){
                $devolucion->estado_devolucion = $request->estado_devolucion;
            }
            if($request->has('persona_recibe')){
                $devolucion->persona_recibe = $request->persona_recibe;
            }
            if($request->has('notas_generales')){
                $devolucion->notas_generales = $request->notas_generales;
            }
            $devolucion->save();
            $devolucion->load('renta');
            $data = [
                'message' => 'devolucion actualizado',
                'devolucion' => $devolucion,
                'status' => 200
            ];
            return response()->json($data, 200);
        }
    }
