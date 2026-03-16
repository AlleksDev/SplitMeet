# Flujo Completo: Confirmación de Pago Biométrico del Participante

## Arquitectura Clean Architecture MVVM implementada

### 1. CAPA DE DATA (Retrofit API)

**Archivo:** `DetailOutingApi.kt`

Nuevo endpoint PATCH agregado:
```kotlin
@PATCH("outings/{outing_id}/participants/{participant_id}/confirm")
suspend fun confirmParticipantPayment(
    @Path("outing_id") outingId: Long,
    @Path("participant_id") participantId: Long
)
```

**Ruta completa:** `PATCH /payments/outings/{outing_id}/participants/{participant_id}/confirm`

---

### 2. CAPA DE DOMAIN (Repositorio + UseCase)

#### Repositorio (`DetailOutingRepository`)
```kotlin
suspend fun confirmParticipantPayment(outingId: Long, participantId: Long)
```

#### Implementación (`DetailOutingRepositoryImpl`)
```kotlin
override suspend fun confirmParticipantPayment(outingId: Long, participantId: Long) {
    api.confirmParticipantPayment(outingId, participantId)
}
```

#### UseCase (`ConfirmParticipantPaymentUseCase`)
- Opera con `Result<Unit>`
- Maneja excepciones automáticamente
- Inyectado en el repositorio
- Sigue el patrón de otros UseCases (SearchUsersUseCase, RemoveParticipantUseCase)

---

### 3. INYECCIÓN DE DEPENDENCIAS

**Archivo:** `DetailOutingUseCaseModule.kt`

- Provider para `ConfirmParticipantPaymentUseCase` agregado
- Registrado en `DetailOutingUseCases` data class
- Compatible con Hilt para inyección automática

---

### 4. CAPA DE PRESENTATION (ViewModel)

**Archivo:** `DetailOutingViewModel.kt`

#### Acceso al UseCase:
```
useCases.confirmParticipantPayment(outingId: Long, participantId: Long)
```

#### Nueva Lógica en `executeConfirmPayment()`:
```kotlin
fun executeConfirmPayment(participant: Participant) {
    _uiState.update { it.copy(confirmingPaymentUserId = participant.userId, error = null) }

    viewModelScope.launch {
        // Usa el endpoint que requiere outingId y participantId
        val result = useCases.confirmParticipantPayment(outingId, participant.id)

        result.fold(
            onSuccess = {
                _uiState.update { it.copy(confirmingPaymentUserId = null) }
                loadParticipants()  // Refresca la lista
                showSuccessMessage("Pago confirmado para @${participant.username}")
            },
            onFailure = { error ->
                _uiState.update { it.copy(confirmingPaymentUserId = null, error = mapOperationError(error)) }
            }
        )
    }
}
```

---

## FLUJO COMPLETO DE EJECUCIÓN

```
┌─────────────────────────────────────────────────────────────────┐
│ 1. DetailOutingScreen - Usuario pulsa "Confirmar Pago"         │
└─────────────────────────────────────────────────────────────────┘
                              ↓
┌─────────────────────────────────────────────────────────────────┐
│ 2. ViewModel.confirmPayment(participant: Participant)           │
│    → Valida que el dispositivo tenga biometría                  │
│    → Actualiza UI: requireBiometricAuth = participant           │
│    → Dispara biometricAuthTrigger (timestamp)                   │
└─────────────────────────────────────────────────────────────────┘
                              ↓
┌─────────────────────────────────────────────────────────────────┐
│ 3. DetailOutingScreen - LaunchedEffect(biometricAuthTrigger)    │
│    → Obtiene FragmentActivity con findFragmentActivity()        │
│    → Llama: viewModel.authenticatePendingPayment(activity)      │
└─────────────────────────────────────────────────────────────────┘
                              ↓
┌─────────────────────────────────────────────────────────────────┐
│ 4. FingerPrintManager.authenticate()                            │
│    → Muestra diálogo biométrico de Android                      │
│    → Usuario toca huella dactilar                               │
└─────────────────────────────────────────────────────────────────┘
                              ↓
            ┌────────────────┴────────────────┐
            ↓                                 ↓
    [HUELLA VÁLIDA]                   [HUELLA INVÁLIDA/ERROR]
            ↓                                 ↓
┌─────────────────────────────────┐  ┌──────────────────────────┐
│ 5. onSuccess callback:          │  │ onError/onFailed:        │
│    → viewModel.                 │  │ → onBiometricAuthError() │
│      onBiometricAuthDismissed() │  │ → Muestra error Snackbar │
│    → viewModel.                 │  │ → Cancela operación      │
│      executeConfirmPayment()    │  └──────────────────────────┘
└─────────────────────────────────┘
            ↓
┌─────────────────────────────────────────────────────────────────┐
│ 6. Llamada al UseCase:                                          │
│    useCases.confirmParticipantPayment(outingId, participantId)  │
│                                                                 │
│    Secuencia:                                                   │
│    ├─ UseCase.invoke()                                          │
│    ├─ Repository.confirmParticipantPayment()                    │
│    └─ Api.confirmParticipantPayment() [RETROFIT]                │
│       └─ PATCH /payments/outings/{id}/participants/{id}/confirm │
└─────────────────────────────────────────────────────────────────┘
            ↓
        [RESPUESTA DEL SERVIDOR]
            ↓
        ¿Éxito?
   Sí ↓            ↓ No
      │            └─→ Result.failure(exception)
      ↓                    ↓
  Result.success()  mapOperationError()
      ↓                    ↓
      │            Error en UI + Snackbar
      ↓
┌─────────────────────────────────────────────────────────────────┐
│ 7. Resultado exitoso:                                           │
│    → confirmingPaymentUserId = null                             │
│    → selectedParticipantId = null                               │
│    → loadParticipants() [REFRESCA LISTA]                        │
│    → showSuccessMessage("Pago confirmado...")                   │
│    → Muestra Snackbar con éxito por 3 segundos                  │
└─────────────────────────────────────────────────────────────────┘
```

---

## CARACTERÍSTICAS IMPLEMENTADAS

✅ **Bajo Acoplamiento**: ViewModel solo conoce la interfaz FingerPrintManager
✅ **Manejo de Estados**: Loading, Success, Error gestionados en UiState
✅ **Inyección de Dependencias**: Hilt + Dagger2 completamente integrados
✅ **Result API**: Manejo de errores con Result<T>
✅ **Refresco de Datos**: loadParticipants() después de confirmación exitosa
✅ **Mensajes de Éxito**: Snackbar con autocancelación a los 3 segundos
✅ **Validaciones**: Verifica biometría antes de disparar autenticación
✅ **Manejo de Errores HTTP**: 401, 400, y errores genéricos mapeados

---

## ARCHIVOS MODIFICADOS

| Archivo | Cambio |
|---------|--------|
| `DetailOutingApi.kt` | ✅ Nuevo endpoint PATCH agregado |
| `DetailOutingRepository.kt` | ✅ Nuevo método en interfaz |
| `DetailOutingRepositoryImpl.kt` | ✅ Implementación del nuevo método |
| `ConfirmParticipantPaymentUseCase.kt` | ✅ Nuevo archivo creado |
| `DetailOutingUseCaseModule.kt` | ✅ Registrado provider y agregado a contenedor |
| `DetailOutingUseCases.kt` | ✅ Nuevo campo en data class |
| `DetailOutingViewModel.kt` | ✅ Nueva lógica en executeConfirmPayment() |

---

## DIAGRAMA DE COMPONENTES

```
┌─────────────────────┐
│  DetailOutingScreen │ (Jetpack Compose UI)
└──────────┬──────────┘
           │ confirmPayment()
           ↓
┌─────────────────────────┐
│ DetailOutingViewModel   │ (MVVM ViewModel)
└──────────┬──────────────┘
           │ useCases
           ↓
┌──────────────────────────────────┐
│ DetailOutingUseCases             │ (Wrapper de UseCases)
├──────────────────────────────────┤
│ • confirmParticipantPayment      │ ← NUEVO
│ • confirmPayment                 │
│ • addParticipant                 │
│ • removeParticipant              │
│ ... etc                          │
└──────────┬───────────────────────┘
           │ invoke()
           ↓
┌──────────────────────────────────────────┐
│ ConfirmParticipantPaymentUseCase         │ ← NUEVO
└──────────┬───────────────────────────────┘
           │ repository
           ↓
┌──────────────────────────────────────────┐
│ DetailOutingRepository (Interface)       │
└──────────┬───────────────────────────────┘
           │ implementation
           ↓
┌──────────────────────────────────────────┐
│ DetailOutingRepositoryImpl                │
└──────────┬───────────────────────────────┘
           │ api
           ↓
┌──────────────────────────────────────────┐
│ DetailOutingApi (Retrofit Interface)     │
└──────────┬───────────────────────────────┘
           │ HTTP PATCH
           ↓
┌──────────────────────────────────────────┐
│ Backend: /payments/outings/{id}/         │
│          participants/{id}/confirm       │
└──────────────────────────────────────────┘
```

---

## INTEGRACIÓN CON BIOMETRÍA

El flujo de biometría ya existente es completamente compatible:

1. **FingerPrintManager** (Hardware Domain) - Interfaz que abstrae el hardware
2. **AndroidFingerPrintManager** (Hardware Data) - Implementación con BiometricPrompt
3. **DetailOutingScreen** - Obtiene FragmentActivity para la autenticación
4. **DetailOutingViewModel** - Orquesta el flujo y maneja callbacks

El nuevo UseCase se ejecuta SOLO después de que la biometría es exitosa.

---

## CONSIDERACIONES DE ARQUITECTURA

### Separación de Responsabilidades
- **UI Layer**: DetailOutingScreen solo dispara eventos
- **Presentation Layer**: ViewModel coordina y maneja estados
- **Domain Layer**: UseCases contienen lógica de negocio
- **Data Layer**: Repository con Retrofit para API calls

### Reutilización
- El generador `operator fun invoke()` permite llamadas simples: `useCase(args)`
- El patrón Result<T> es consistente en toda la arquitectura
- Errores se mapean centralmente en el ViewModel

### Testing
Cada capa es independiente y testeable:
- Mock del Repository para probar ViewModel
- Mock del Api para probar Repository
- Mock de FingerPrintManager para probar flujo completo
