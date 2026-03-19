<?php

namespace App\Http\Controllers\api;

use App\Http\Controllers\Controller;
use App\Models\Entrega;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Validator;
use Illuminate\Support\Facades\Log;
use Illuminate\Validation\Rule;

class entregaController extends Controller
{
    /**
     * Lista todas las entregas programadas o realizadas.
     */
        public function index(){
        $entrega = Entrega::all();
        $data = [
            'entrega' => $entrega,
            'status' => 200
        ];
        return response()->json($data, 200);
        }
    /**
     * Programa o registra una nueva entrega.
     * 
     * ANALOGÍA: Esta función es como generar una guía de paquetería. 
     * Se define qué se envía (renta), a dónde (dirección), cuándo y quién lo lleva (compañía de envío).
     */
        public function store(Request $request)
        {
            try {
            $validator = Validator::make($request->all(),[
                'renta_id' => ['required', Rule::exists('renta', 'id')],
                'direccion_id' => ['required', Rule::exists('direccion', 'id')],
                'fecha_envio' => ['required', 'date'],
                'compania_envio' => ['required', 'string'],
                'numero_guia' => ['required', 'string'],
                'estado_entrega' => ['required', Rule::in(['Programada','EnTransito','Entregada','Fallida'])],
                'notas' => ['required', 'string']
            ]);
            if($validator->fails()){
                $data = [
                    'message' => 'Validation failed',
                    'errors' => $validator->errors(),
                    'status' => 400
                ];
                return response()->json($data, 400);
            }
            $entrega = Entrega::create([
                'renta_id' => $request->renta_id,
                'direccion_id' => $request->direccion_id,
                'fecha_envio' => $request->fecha_envio,
                'compania_envio' => $request->compania_envio,
                'numero_guia' => $request->numero_guia,
                'estado_entrega' => $request->estado_entrega,
                'notas' => $request->notas,
            ]);
            if(!$entrega){
                $data = [
                    'message' => 'Error al crear entrega',
                    'status' => 500
                ];
                return response()->json($data, 500);
            }
            $entrega->load('renta', 'direccion');
            $data = [
                'entrega' => $entrega,
                'status' => 200
            ];
            return response()->json($data, 200);

            } catch (\Exception $e) {
                Log::error('Error al crear Entrega: ' . $e->getMessage());
                return response()->json([
                    'message' => 'Error interno al crear la entrega',
                    'error' => $e->getMessage(),
                    'status' => 500
                ], 500);
            }
        }
    
    /**
     * Muestra los detalles de una entrega específica.
     */
        public function show($id){
            $entrega = Entrega::find($id);
            if(!$entrega){
                $data = [
                    'message' => 'Entrega no encontrada',
                    'status' => 404
                ];
                return response()->json($data, 404);
            }
            $data = [
                'entrega' => $entrega,
                'status' => 200
            ];
            return response()->json($data, 200);
        }
    /**
     * Elimina un registro de entrega.
     */
        public function destroy($id){
            $entrega = Entrega::find($id);
            if(!$entrega){
                $data = [
                    'message' => 'Entrega no encontrada',
                    'status' => 404
                ];
                return response()->json($data, 404);
            }
            $entrega->delete();
            $data = [
                'entrega' => $entrega,
                'status' => 200
            ];
            return response()->json($data, 200);
        }
    /**
     * Actualiza la información de una entrega.
     */
        public function update(Request $request, $id){
            try {
                $entrega = Entrega::find($id);
                if(!$entrega){
                    $data = [
                        'message' => 'Entrega no encontrada',
                        'status' => 404
                ];
                return response()->json($data, 404);
            }
            $validator = Validator::make($request->all(),[
                'renta_id' => ['required', Rule::exists('renta', 'id')],
                'direccion_id' => ['required', Rule::exists('direccion', 'id')],
                'fecha_envio' => ['required', 'date'],
                'compania_envio' => ['required', 'string'],
                'numero_guia' => ['required', 'string'],
                'estado_entrega' => ['required', Rule::in(['Programada','EnTransito','Entregada','Fallida'])],
                'notas' => ['required', 'string']
            ]);
            if($validator->fails()){
                $data = [
                    'message' => 'Validator failed',
                    'errors' => $validator->errors(),
                    'status' => 201
                ];
                return response()->json($data, 201);
            }
            $entrega->renta_id = $request->renta_id;
            $entrega->direccion_id = $request->direccion_id;
            $entrega->fecha_envio = $request->fecha_envio;
            $entrega->compania_envio = $request->compania_envio;
            $entrega->numero_guia = $request->numero_guia;
            $entrega->estado_entrega = $request->estado_entrega;
            $entrega->notas = $request->notas;
            $entrega->save();
            $entrega->load('renta', 'direccion');
            $data = [
                'message' => 'entrega actualizado',
                'entrega' => $entrega,
                'status' => 200
            ];
            return response()->json($data, 200);
            
            } catch (\Exception $e) {
                Log::error('Error al actualizar Entrega: ' . $e->getMessage());
                return response()->json([
                    'message' => 'Error interno al actualizar la entrega',
                    'error' => $e->getMessage(),
                    'status' => 500
                ], 500);
            }
        }
    /**
     * Actualización parcial de una entrega.
     */
        public function updatePartial(Request $request, $id)
        {
            try {
            $entrega = Entrega::find($id);
            if(!$entrega){
                $data = [
                    'message' => 'entrega no encontrada',
                    'status' => 404
                ];
                return response()->json($data, 404);
            }
            $validator = Validator::make($request->all(),[
                'renta_id' => [ Rule::exists('renta','id')],
                'direccion_id' => [ Rule::exists('direccion','id')],
                'fecha_envio' => ['date'],
                'compania_envio' => ['string'],
                'numero_guia' => ['string'],
                'estado_entrega' => ['string', Rule::in(['Programada','EnTransito','Entregada','Fallida'])],
                'notas' => ['string']
            ]);
            
            if($validator->fails()){
                $data = [
                    'message' => 'Error en la Validacion',
                    'errors' => $validator->errors(),
                    'status' => 201
                ];
                return response()->json($data, 201);
            }
            
            if($request->has('renta_id')){
                $entrega->renta_id = $request->renta_id;
            }
            if($request->has('direccion_id')){
                $entrega->direccion_id = $request->direccion_id;
            }
            if($request->has('fecha_envio')){
                $entrega->fecha_envio = $request->fecha_envio;
            }
            if($request->has('compania_envio')){
                $entrega->compania_envio = $request->compania_envio;
            }
            if($request->has('numero_guia')){
                $entrega->numero_guia = $request->numero_guia;
            }
            if($request->has('estado_entrega')){
                $entrega->estado_entrega = $request->estado_entrega;
            }
            if($request->has('notas')){
                $entrega->notas = $request->notas;
            }
            $entrega->save();
            $entrega->load('renta', 'direccion');
            $data = [
                'message' => 'entrega actualizado',
                'entrega' => $entrega,
                'status' => 200
            ];
            return response()->json($data, 200);
            } catch (\Exception $e) {
                Log::error('Error al actualizar Entrega: ' . $e->getMessage());
                return response()->json([
                    'message' => 'Error interno al actualizar la entrega',
                    'error' => $e->getMessage(),
                    'status' => 500
                ], 500);
            }
        }
    }
