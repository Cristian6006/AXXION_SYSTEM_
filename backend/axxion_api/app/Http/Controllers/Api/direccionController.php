<?php

namespace App\Http\Controllers\api;

use App\Http\Controllers\Controller;
use App\Models\Direccion;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Validator;

class direccionController extends Controller
{
    /**
     * Lista todas las direcciones registradas.
     */
    public function index()
    {
        $direccion = Direccion::all();
        $data = [
            'direccion' => $direccion,
            'status' => 200
        ];
        return response()->json($data, 200);
    }
    /**
     * Registra una nueva dirección en el sistema.
     * 
     * ANALOGÍA: Esta función actúa como el catastro municipal. 
     * Registra una nueva ubicación física con todos sus detalles (calle, número, colonia) 
     * para que el sistema sepa dónde entregar o recoger equipos.
     */
    public function store(Request $request)
    {
        $validator = Validator::make($request->all(), [
            'calle' => 'required',
            'numero_exterior' => 'required',
            'numero_interior' => 'required',
            'colonia' => 'required',
            'ciudad' => 'required',
            'estado_provincia' => 'required',
            'codigo_postal' => 'required',
            'pais' => 'required',
            'referencias' => 'required',
        ]);
        if ($validator->fails()) {
            $data = [
                'message' => 'Validation failed',
                'errors' => $validator->errors(),
                'status' => 400
            ];
            return response()->json($data, 400);
        }
        $direccion = Direccion::create([
            'calle' => $request->calle,
            'numero_exterior' => $request->numero_exterior,
            'numero_interior' => $request->numero_interior,
            'colonia' => $request->colonia,
            'ciudad' => $request->ciudad,
            'estado_provincia' => $request->estado_provincia,
            'codigo_postal' => $request->codigo_postal,
            'pais' => $request->pais,
            'referencias' => $request->referencias,
        ]);
        if (!$direccion) {
            $data = [
                'message' => 'Error al crear direccion',
                'status' => 500
            ];
            return response()->json($data, 500);
        }
        $data = [
            'direccion' => $direccion,
            'status' => 200
        ];
        return response()->json($data, 200);
    }
    /**
     * Muestra los detalles de una dirección específica.
     */
    public function show($id)
    {
        $direccion = Direccion::find($id);
        if (!$direccion) {
            $data = [
                'message' => 'Direccion no encontrada',
                'status' => 404
            ];
            return response()->json($data, 404);
        }
        $data = [
            'direccion' => $direccion,
            'status' => 200
        ];
        return response()->json($data, 200);
    }
    /**
     * Elimina una dirección del sistema.
     */
    public function destroy($id)
    {
        $direccion = Direccion::find($id);
        if (!$direccion) {
            $data = [
                'message' => 'Direccion no encontrada',
                'status' => 404
            ];
            return response()->json($data, 404);
        }
        $direccion->delete();
        $data = [
            'direccion' => $direccion,
            'status' => 200
        ];
        return response()->json($data, 200);
    }
    /**
     * Actualiza la información de una dirección.
     */
    public function update(Request $request, $id)
    {
        $direccion = Direccion::find($id);
        if (!$direccion) {
            $data = [
                'message' => 'Direccion no encontrada',
                'status' => 404
            ];
            return response()->json($data, 404);
        }
        $validator = Validator::make($request->all(), [
            'calle' => 'required',
            'numero_exterior' => 'required',
            'numero_interior' => 'required',
            'colonia' => 'required',
            'ciudad' => 'required',
            'estado_provincia' => 'required',
            'codigo_postal' => 'required',
            'pais' => 'required',
            'referencias' => 'required',
        ]);
        if ($validator->fails()) {
            $data = [
                'message' => 'Validator failed',
                'errors' => $validator->errors(),
                'status' => 400
            ];
            return response()->json($data, 400);
        }
        $direccion->calle = $request->calle;
        $direccion->numero_exterior = $request->numero_exterior;
        $direccion->numero_interior = $request->numero_interior;
        $direccion->colonia = $request->colonia;
        $direccion->ciudad = $request->ciudad;
        $direccion->estado_provincia = $request->estado_provincia;
        $direccion->codigo_postal = $request->codigo_postal;
        $direccion->pais = $request->pais;
        $direccion->referencias = $request->referencias;
        $direccion->save();
        $data = [
            'message' => 'direccion actualizado',
            'direccion' => $direccion,
            'status' => 200
        ];
        return response()->json($data, 200);
    }
    /**
     * Actualización parcial de una dirección.
     */
    public function updatePartial(Request $request, $id)
    {
        $direccion = Direccion::find($id);
        if (!$direccion) {
            $data = [
                'message' => 'direccion no encontrada',
                'status' => 404
            ];
            return response()->json($data, 404);
        }
        $validator = Validator::make($request->all(), [
            'calle' => 'string',
            'numero_exterior' => 'string',
            'numero_interior' => 'string',
            'colonia' => 'string',
            'ciudad' => 'string',
            'estado_provincia' => 'string',
            'codigo_postal' => 'string',
            'pais' => 'string',
            'referencias' => 'string',
        ]);

        if ($validator->fails()) {
            $data = [
                'message' => 'Error en la Validacion',
                'errors' => $validator->errors(),
                'status' => 400
            ];
            return response()->json($data, 400);
        }

        if ($request->has('calle')) {
            $direccion->calle = $request->calle;
        }
        if ($request->has('numero_exterior')) {
            $direccion->numero_exterior = $request->numero_exterior;
        }
        if ($request->has('numero_interior')) {
            $direccion->numero_interior = $request->numero_interior;
        }
        if ($request->has('colonia')) {
            $direccion->colonia = $request->colonia;
        }
        if ($request->has('ciudad')) {
            $direccion->ciudad = $request->ciudad;
        }
        if ($request->has('estado_provincia')) {
            $direccion->estado_provincia = $request->estado_provincia;
        }
        if ($request->has('codigo_postal')) {
            $direccion->codigo_postal = $request->codigo_postal;
        }
        if ($request->has('pais')) {
            $direccion->pais = $request->pais;
        }
        if ($request->has('referencias')) {
            $direccion->referencias = $request->referencias;
        }
        $direccion->save();
        $data = [
            'message' => 'direccion actualizado',
            'direccion' => $direccion,
            'status' => 200
        ];
        return response()->json($data, 200);
    }
}
