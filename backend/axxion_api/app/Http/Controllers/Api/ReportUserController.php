<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Models\Usuario;
use App\Models\Rol;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Log;
use Illuminate\Support\Facades\DB;

class ReportUserController extends Controller
{
    /**
     * Reporte de Distribución por Departamento
     * 
     * Objetivo: Identificar qué áreas demandan más usuarios en el sistema.
     * 
     * Datos:
     * - Agrupación por departamento
     * - Conteo de usuarios por departamento
     * - Porcentaje de distribución
     * 
     * @return \Illuminate\Http\JsonResponse
     */
    public function distribucionPorDepartamento()
    {
        try {
            // Agrupamos usuarios por departamento y contamos
            $distribucion = Usuario::select(
                'departamento',
                DB::raw('COUNT(id) as cantidad_usuarios')
            )
                ->groupBy('departamento')
                ->orderByDesc('cantidad_usuarios')
                ->get();

            $totalUsuarios = $distribucion->sum('cantidad_usuarios');

            // Añadimos porcentaje a cada departamento
            $distribucionConPorcentaje = $distribucion->map(function ($item) use ($totalUsuarios) {
                return [
                    'departamento' => $item->departamento ?? 'Sin Departamento',
                    'cantidad_usuarios' => $item->cantidad_usuarios,
                    'porcentaje' => $totalUsuarios > 0
                        ? round(($item->cantidad_usuarios / $totalUsuarios) * 100, 2)
                        : 0,
                ];
            });

            // Identificar departamento con mayor demanda
            $departamentoMaximo = $distribucionConPorcentaje->first();

            // Resumen general
            $resumen = [
                'total_usuarios' => $totalUsuarios,
                'total_departamentos' => $distribucion->count(),
                'departamento_mayor_demanda' => $departamentoMaximo ? [
                    'nombre' => $departamentoMaximo['departamento'],
                    'cantidad' => $departamentoMaximo['cantidad_usuarios'],
                    'porcentaje' => $departamentoMaximo['porcentaje'] . '%',
                ] : null,
                'promedio_usuarios_por_departamento' => $distribucion->count() > 0
                    ? round($totalUsuarios / $distribucion->count(), 2)
                    : 0,
            ];

            return response()->json([
                'resumen' => $resumen,
                'distribucion_por_departamento' => $distribucionConPorcentaje,
            ]);

        } catch (\Exception $e) {
            Log::error('Error en reporte de distribución por departamento: ' . $e->getMessage());
            return response()->json([
                'error' => 'Error al generar reporte de distribución por departamento',
                'mensaje' => $e->getMessage()
            ], 500);
        }
    }

    /**
     * Reporte de Seguridad y Roles
     * 
     * Objetivo: Identificar cuántos Administradores, Técnicos o Empleados hay activos vs. inactivos.
     * 
     * Datos:
     * - Relación roles() + estado
     * - Conteo por rol y estado (activo/inactivo)
     * - Alertas de seguridad (administradores inactivos)
     * 
     * @return \Illuminate\Http\JsonResponse
     */
    public function seguridadYRoles()
    {
        try {
            // Obtenemos todos los roles con sus usuarios
            $roles = Rol::with([
                'usuarios' => function ($query) {
                    $query->select(
                        'usuario.id',
                        'usuario.nombre_usuario',
                        'usuario.nombre',
                        'usuario.apellido1',
                        'usuario.email',
                        'usuario.departamento',
                        'usuario.estado'
                    );
                }
            ])->get();

            // Conteo por rol y estado
            $conteoPorRolYEstado = $roles->map(function ($rol) {
                $usuariosPorRol = $rol->usuarios;
                $activos = $usuariosPorRol->where('estado', 'activo')->count();
                $inactivos = $usuariosPorRol->where('estado', 'inactivo')->count();
                $total = $usuariosPorRol->count();

                return [
                    'rol_id' => $rol->id,
                    'rol_codigo' => $rol->codigo,
                    'rol_nombre' => $rol->nombre,
                    'total_usuarios' => $total,
                    'activos' => $activos,
                    'inactivos' => $inactivos,
                    'porcentaje_activos' => $total > 0 ? round(($activos / $total) * 100, 2) : 0,
                    'porcentaje_inactivos' => $total > 0 ? round(($inactivos / $total) * 100, 2) : 0,
                ];
            });

            // === ALERTA DE SEGURIDAD: Administradores Inactivos ===
            // Buscamos usuarios con rol "Administrador" que estén inactivos
            $rolesAdministrador = ['ADMIN', 'Administrador', 'admin', 'ADM']; // Códigos posibles

            $administradoresInactivos = Usuario::whereHas('roles', function ($query) use ($rolesAdministrador) {
                $query->whereIn('codigo', $rolesAdministrador)
                    ->orWhere('nombre', 'LIKE', '%Administrador%')
                    ->orWhere('nombre', 'LIKE', '%Admin%');
            })
                ->where('estado', 'inactivo')
                ->select('id', 'nombre_usuario', 'nombre', 'apellido1', 'email', 'departamento', 'estado', 'created_at', 'updated_at')
                ->with(['roles:id,codigo,nombre'])
                ->get();

            // Formateamos la alerta de administradores inactivos
            $alertaAdminInactivos = $administradoresInactivos->map(function ($usuario) {
                return [
                    'id' => $usuario->id,
                    'nombre_usuario' => $usuario->nombre_usuario,
                    'nombre_completo' => trim("{$usuario->nombre} {$usuario->apellido1}"),
                    'email' => $usuario->email,
                    'departamento' => $usuario->departamento,
                    'estado' => $usuario->estado,
                    'roles' => $usuario->roles->pluck('nombre')->toArray(),
                    'fecha_creacion' => $usuario->created_at ? $usuario->created_at->format('Y-m-d') : null,
                    'ultima_actualizacion' => $usuario->updated_at ? $usuario->updated_at->format('Y-m-d') : null,
                    'riesgo' => 'ALTO - Cuenta administrativa inactiva (posible acceso residual o basura en BD)',
                ];
            });

            // Totales generales
            $totalUsuarios = Usuario::count();
            $usuariosActivos = Usuario::where('estado', 'activo')->count();
            $usuariosInactivos = Usuario::where('estado', 'inactivo')->count();

            // Nivel de riesgo general
            $cantidadAdminInactivos = $alertaAdminInactivos->count();
            $nivelRiesgo = $cantidadAdminInactivos === 0
                ? 'BAJO'
                : ($cantidadAdminInactivos <= 3
                    ? 'MEDIO'
                    : 'ALTO');

            $resumen = [
                'total_usuarios' => $totalUsuarios,
                'usuarios_activos' => $usuariosActivos,
                'usuarios_inactivos' => $usuariosInactivos,
                'porcentaje_activos' => $totalUsuarios > 0 ? round(($usuariosActivos / $totalUsuarios) * 100, 2) . '%' : '0%',
                'porcentaje_inactivos' => $totalUsuarios > 0 ? round(($usuariosInactivos / $totalUsuarios) * 100, 2) . '%' : '0%',
                'total_roles' => $roles->count(),
                'seguridad' => [
                    'administradores_inactivos' => $cantidadAdminInactivos,
                    'nivel_riesgo' => $nivelRiesgo,
                    'alerta' => $cantidadAdminInactivos > 0
                        ? "ATENCIÓN: Se detectaron {$cantidadAdminInactivos} cuenta(s) de Administrador inactiva(s). Esto representa un riesgo de seguridad latente. Se recomienda revisar y eliminar o reactivar estas cuentas."
                        : 'No se detectaron cuentas administrativas inactivas.',
                    'recomendacion' => $cantidadAdminInactivos > 0
                        ? 'Revisar las cuentas listadas en alertas_administradores_inactivos para determinar si deben ser eliminadas definitivamente o reactivadas.'
                        : 'Mantener monitoreo periódico de cuentas administrativas.',
                ],
            ];

            return response()->json([
                'resumen' => $resumen,
                'distribucion_roles_por_estado' => $conteoPorRolYEstado,
                'alertas_administradores_inactivos' => $alertaAdminInactivos,
            ]);

        } catch (\Exception $e) {
            Log::error('Error en reporte de seguridad y roles: ' . $e->getMessage());
            return response()->json([
                'error' => 'Error al generar reporte de seguridad y roles',
                'mensaje' => $e->getMessage()
            ], 500);
        }
    }

    /**
     * Reporte Combinado de Usuarios
     * 
     * Combina ambos reportes: distribución por departamento + seguridad y roles
     * Proporciona una visión completa del estado de usuarios del sistema.
     * 
     * @return \Illuminate\Http\JsonResponse
     */
    public function reporteCompleto()
    {
        try {
            // === DISTRIBUCIÓN POR DEPARTAMENTO ===
            $distribucion = Usuario::select(
                'departamento',
                DB::raw('COUNT(id) as cantidad_usuarios')
            )
                ->groupBy('departamento')
                ->orderByDesc('cantidad_usuarios')
                ->get();

            $totalUsuarios = Usuario::count();

            $distribucionDepartamentos = $distribucion->map(function ($item) use ($totalUsuarios) {
                return [
                    'departamento' => $item->departamento ?? 'Sin Departamento',
                    'cantidad_usuarios' => $item->cantidad_usuarios,
                    'porcentaje' => $totalUsuarios > 0
                        ? round(($item->cantidad_usuarios / $totalUsuarios) * 100, 2)
                        : 0,
                ];
            });

            // === SEGURIDAD Y ROLES ===
            $roles = Rol::with(['usuarios'])->get();

            $conteoPorRol = $roles->map(function ($rol) {
                $usuariosPorRol = $rol->usuarios;
                return [
                    'rol_codigo' => $rol->codigo,
                    'rol_nombre' => $rol->nombre,
                    'total' => $usuariosPorRol->count(),
                    'activos' => $usuariosPorRol->where('estado', 'activo')->count(),
                    'inactivos' => $usuariosPorRol->where('estado', 'inactivo')->count(),
                ];
            });

            // Administradores inactivos
            $rolesAdministrador = ['ADMIN', 'Administrador', 'admin', 'ADM'];

            $adminInactivos = Usuario::whereHas('roles', function ($query) use ($rolesAdministrador) {
                $query->whereIn('codigo', $rolesAdministrador)
                    ->orWhere('nombre', 'LIKE', '%Administrador%')
                    ->orWhere('nombre', 'LIKE', '%Admin%');
            })
                ->where('estado', 'inactivo')
                ->select('id', 'nombre_usuario', 'nombre', 'apellido1', 'email', 'departamento')
                ->get();

            $usuariosActivos = Usuario::where('estado', 'activo')->count();
            $usuariosInactivos = Usuario::where('estado', 'inactivo')->count();

            $resumenGeneral = [
                'fecha_reporte' => now()->format('Y-m-d H:i:s'),
                'usuarios' => [
                    'total' => $totalUsuarios,
                    'activos' => $usuariosActivos,
                    'inactivos' => $usuariosInactivos,
                ],
                'departamentos' => [
                    'total' => $distribucion->count(),
                    'mayor_demanda' => $distribucionDepartamentos->first()['departamento'] ?? 'N/A',
                ],
                'seguridad' => [
                    'administradores_inactivos' => $adminInactivos->count(),
                    'nivel_riesgo' => $adminInactivos->count() === 0 ? 'BAJO' : ($adminInactivos->count() <= 3 ? 'MEDIO' : 'ALTO'),
                ],
            ];

            return response()->json([
                'resumen_general' => $resumenGeneral,
                'distribucion_departamentos' => $distribucionDepartamentos,
                'distribucion_roles' => $conteoPorRol,
                'alertas_seguridad' => [
                    'administradores_inactivos' => $adminInactivos,
                ],
            ]);

        } catch (\Exception $e) {
            Log::error('Error en reporte completo de usuarios: ' . $e->getMessage());
            return response()->json([
                'error' => 'Error al generar reporte completo de usuarios',
                'mensaje' => $e->getMessage()
            ], 500);
        }
    }

    /**
     * Curva de Crecimiento de Usuarios
     * 
     * Objetivo: Visualizar qué tan rápido está creciendo la organización o la adopción del sistema.
     * 
     * Datos:
     * - created_at agrupado por mes/año
     * - Conteo acumulativo de usuarios
     * - Tasa de crecimiento mes a mes
     * 
     * Analogía: Es como mirar el velocímetro del coche; te dice si estás acelerando (contratando mucho) o frenando.
     * 
     * @return \Illuminate\Http\JsonResponse
     */
    public function curvaCrecimientoUsuarios()
    {
        try {
            // Agrupamos usuarios por mes/año de creación
            $crecimientoPorMes = Usuario::select(
                DB::raw('YEAR(created_at) as anio'),
                DB::raw('MONTH(created_at) as mes'),
                DB::raw('COUNT(id) as usuarios_nuevos')
            )
                ->whereNotNull('created_at')
                ->groupBy(DB::raw('YEAR(created_at)'), DB::raw('MONTH(created_at)'))
                ->orderBy(DB::raw('YEAR(created_at)'))
                ->orderBy(DB::raw('MONTH(created_at)'))
                ->get();

            // Calculamos acumulativo y tasa de crecimiento
            $acumulado = 0;
            $mesAnterior = null;
            $nombresMeses = [
                1 => 'Enero',
                2 => 'Febrero',
                3 => 'Marzo',
                4 => 'Abril',
                5 => 'Mayo',
                6 => 'Junio',
                7 => 'Julio',
                8 => 'Agosto',
                9 => 'Septiembre',
                10 => 'Octubre',
                11 => 'Noviembre',
                12 => 'Diciembre'
            ];

            $curvaCrecimiento = $crecimientoPorMes->map(function ($item) use (&$acumulado, &$mesAnterior, $nombresMeses) {
                $usuariosNuevos = $item->usuarios_nuevos;
                $acumulado += $usuariosNuevos;

                // Calculamos tasa de crecimiento respecto al mes anterior
                $tasaCrecimiento = 0;
                if ($mesAnterior !== null && $mesAnterior > 0) {
                    $tasaCrecimiento = round((($usuariosNuevos - $mesAnterior) / $mesAnterior) * 100, 2);
                }

                $resultado = [
                    'periodo' => $nombresMeses[$item->mes] . ' ' . $item->anio,
                    'anio' => $item->anio,
                    'mes' => $item->mes,
                    'mes_nombre' => $nombresMeses[$item->mes],
                    'usuarios_nuevos' => $usuariosNuevos,
                    'usuarios_acumulados' => $acumulado,
                    'tasa_crecimiento_mensual' => $tasaCrecimiento . '%',
                    'tendencia' => $mesAnterior === null
                        ? 'INICIO'
                        : ($usuariosNuevos > $mesAnterior
                            ? 'ACELERANDO'
                            : ($usuariosNuevos < $mesAnterior
                                ? 'DESACELERANDO'
                                : 'ESTABLE')),
                ];

                $mesAnterior = $usuariosNuevos;
                return $resultado;
            });

            // Estadísticas generales
            $totalUsuarios = Usuario::count();
            $promedioMensual = $crecimientoPorMes->avg('usuarios_nuevos');
            $mesMayorCrecimiento = $curvaCrecimiento->sortByDesc('usuarios_nuevos')->first();
            $mesMenorCrecimiento = $curvaCrecimiento->sortBy('usuarios_nuevos')->first();

            // Análisis del último trimestre vs trimestre anterior
            $ultimosMeses = $curvaCrecimiento->take(-3);
            $mesesAnteriores = $curvaCrecimiento->slice(-6, 3);

            $promedioUltimoTrimestre = $ultimosMeses->avg('usuarios_nuevos') ?? 0;
            $promedioTrimestreAnterior = $mesesAnteriores->avg('usuarios_nuevos') ?? 0;

            $tendenciaGeneral = $promedioTrimestreAnterior > 0
                ? round((($promedioUltimoTrimestre - $promedioTrimestreAnterior) / $promedioTrimestreAnterior) * 100, 2)
                : 0;

            // Velocímetro de crecimiento
            $velocimetro = 'NEUTRAL';
            if ($tendenciaGeneral > 20) {
                $velocimetro = 'CRECIMIENTO ACELERADO';
            } elseif ($tendenciaGeneral > 0) {
                $velocimetro = 'CRECIMIENTO MODERADO';
            } elseif ($tendenciaGeneral < -20) {
                $velocimetro = '🔻 DESACELERACIÓN FUERTE';
            } elseif ($tendenciaGeneral < 0) {
                $velocimetro = 'DESACELERACIÓN LEVE';
            }

            $resumen = [
                'total_usuarios_sistema' => $totalUsuarios,
                'meses_analizados' => $crecimientoPorMes->count(),
                'promedio_usuarios_por_mes' => round($promedioMensual, 2),
                'mes_mayor_crecimiento' => $mesMayorCrecimiento ? [
                    'periodo' => $mesMayorCrecimiento['periodo'],
                    'usuarios_nuevos' => $mesMayorCrecimiento['usuarios_nuevos'],
                ] : null,
                'mes_menor_crecimiento' => $mesMenorCrecimiento ? [
                    'periodo' => $mesMenorCrecimiento['periodo'],
                    'usuarios_nuevos' => $mesMenorCrecimiento['usuarios_nuevos'],
                ] : null,
                'analisis_tendencia' => [
                    'promedio_ultimo_trimestre' => round($promedioUltimoTrimestre, 2),
                    'promedio_trimestre_anterior' => round($promedioTrimestreAnterior, 2),
                    'variacion_porcentual' => $tendenciaGeneral . '%',
                    'velocimetro' => $velocimetro,
                ],
            ];

            return response()->json([
                'resumen' => $resumen,
                'curva_crecimiento' => $curvaCrecimiento,
            ]);

        } catch (\Exception $e) {
            Log::error('Error en curva de crecimiento de usuarios: ' . $e->getMessage());
            return response()->json([
                'error' => 'Error al generar curva de crecimiento de usuarios',
                'mensaje' => $e->getMessage()
            ], 500);
        }
    }

    /**
     * Auditoría de Usuarios Fantasma
     * 
     * Objetivo: Listar usuarios que no han actualizado su perfil o no han tenido actividad 
     * en los últimos 6 meses (basado en updated_at).
     * 
     * Datos:
     * - updated_at vs. Fecha Actual
     * - Clasificación por tiempo de inactividad
     * 
     * Acción: Candidatos a ser desactivados para liberar licencias o reducir superficie de ataque.
     * 
     * @param Request $request
     * @return \Illuminate\Http\JsonResponse
     */
    public function auditoriaUsuariosFantasma(Request $request)
    {
        try {
            // Parámetro configurable: meses de inactividad (default: 6)
            $mesesInactividad = $request->input('meses', 6);
            $fechaLimite = now()->subMonths($mesesInactividad);

            // Usuarios fantasma: sin actualización en los últimos X meses
            $usuariosFantasma = Usuario::where(function ($query) use ($fechaLimite) {
                $query->where('updated_at', '<', $fechaLimite)
                    ->orWhereNull('updated_at');
            })
                ->select(
                    'id',
                    'nombre_usuario',
                    'nombre',
                    'apellido1',
                    'email',
                    'departamento',
                    'estado',
                    'created_at',
                    'updated_at'
                )
                ->with(['roles:id,codigo,nombre'])
                ->orderBy('updated_at')
                ->get();

            // Clasificar por nivel de inactividad
            $usuariosClasificados = $usuariosFantasma->map(function ($usuario) {
                $ultimaActividad = $usuario->updated_at;
                $diasInactividad = $ultimaActividad
                    ? now()->diffInDays($ultimaActividad)
                    : now()->diffInDays($usuario->created_at);

                $mesesInactividad = floor($diasInactividad / 30);

                // Clasificación por riesgo
                $nivelRiesgo = 'BAJO';
                $accionRecomendada = 'Monitorear';

                if ($mesesInactividad >= 12) {
                    $nivelRiesgo = 'CRÍTICO';
                    $accionRecomendada = 'Desactivar inmediatamente';
                } elseif ($mesesInactividad >= 9) {
                    $nivelRiesgo = 'ALTO';
                    $accionRecomendada = 'Contactar usuario y considerar desactivación';
                } elseif ($mesesInactividad >= 6) {
                    $nivelRiesgo = 'MEDIO';
                    $accionRecomendada = 'Enviar recordatorio de activación';
                }

                return [
                    'id' => $usuario->id,
                    'nombre_usuario' => $usuario->nombre_usuario,
                    'nombre_completo' => trim("{$usuario->nombre} {$usuario->apellido1}"),
                    'email' => $usuario->email,
                    'departamento' => $usuario->departamento,
                    'estado_actual' => $usuario->estado,
                    'roles' => $usuario->roles->pluck('nombre')->toArray(),
                    'fecha_creacion' => $usuario->created_at ? $usuario->created_at->format('Y-m-d') : 'N/A',
                    'ultima_actividad' => $ultimaActividad ? $ultimaActividad->format('Y-m-d H:i:s') : 'NUNCA',
                    'dias_inactividad' => $diasInactividad,
                    'meses_inactividad' => $mesesInactividad,
                    'nivel_riesgo' => $nivelRiesgo,
                    'accion_recomendada' => $accionRecomendada,
                ];
            });

            // Estadísticas de inactividad
            $totalFantasmas = $usuariosClasificados->count();
            $totalUsuarios = Usuario::count();
            $porcentajeFantasmas = $totalUsuarios > 0
                ? round(($totalFantasmas / $totalUsuarios) * 100, 2)
                : 0;

            // Agrupación por nivel de riesgo
            $porNivelRiesgo = [
                'critico' => $usuariosClasificados->where('nivel_riesgo', 'CRÍTICO')->count(),
                'alto' => $usuariosClasificados->where('nivel_riesgo', 'ALTO')->count(),
                'medio' => $usuariosClasificados->where('nivel_riesgo', 'MEDIO')->count(),
                'bajo' => $usuariosClasificados->where('nivel_riesgo', 'BAJO')->count(),
            ];

            // Agrupación por departamento (para ver qué áreas tienen más fantasmas)
            $porDepartamento = $usuariosClasificados
                ->groupBy('departamento')
                ->map(function ($grupo, $depto) {
                    return [
                        'departamento' => $depto ?? 'Sin Departamento',
                        'cantidad' => $grupo->count(),
                    ];
                })
                ->sortByDesc('cantidad')
                ->values();

            // Usuarios activos que son fantasmas (alto riesgo de seguridad)
            $fantasmasActivos = $usuariosClasificados
                ->where('estado_actual', 'activo')
                ->where('nivel_riesgo', '!=', 'BAJO');

            // Alerta de seguridad
            $alertaSeguridad = $fantasmasActivos->count() > 0
                ? "ALERTA: Se encontraron {$fantasmasActivos->count()} usuario(s) con estado ACTIVO pero sin actividad significativa. Representan una superficie de ataque potencial."
                : 'No se detectaron usuarios activos con inactividad prolongada.';

            $resumen = [
                'fecha_analisis' => now()->format('Y-m-d H:i:s'),
                'parametros' => [
                    'meses_inactividad_minimo' => $mesesInactividad,
                    'fecha_limite' => $fechaLimite->format('Y-m-d'),
                ],
                'estadisticas' => [
                    'total_usuarios_sistema' => $totalUsuarios,
                    'usuarios_fantasma_detectados' => $totalFantasmas,
                    'porcentaje_fantasmas' => $porcentajeFantasmas . '%',
                    'promedio_dias_inactividad' => round($usuariosClasificados->avg('dias_inactividad'), 0),
                ],
                'clasificacion_por_riesgo' => $porNivelRiesgo,
                'seguridad' => [
                    'fantasmas_con_estado_activo' => $fantasmasActivos->count(),
                    'alerta' => $alertaSeguridad,
                ],
                'recomendaciones' => [
                    'inmediatas' => "Desactivar {$porNivelRiesgo['critico']} usuario(s) con riesgo CRÍTICO",
                    'corto_plazo' => "Revisar {$porNivelRiesgo['alto']} usuario(s) con riesgo ALTO",
                    'mediano_plazo' => "Contactar {$porNivelRiesgo['medio']} usuario(s) con riesgo MEDIO",
                ],
            ];

            return response()->json([
                'resumen' => $resumen,
                'distribucion_por_departamento' => $porDepartamento,
                'usuarios_fantasma' => $usuariosClasificados,
                'alertas_criticas' => $fantasmasActivos->values(),
            ]);

        } catch (\Exception $e) {
            Log::error('Error en auditoría de usuarios fantasma: ' . $e->getMessage());
            return response()->json([
                'error' => 'Error al generar auditoría de usuarios fantasma',
                'mensaje' => $e->getMessage()
            ], 500);
        }
    }
}
