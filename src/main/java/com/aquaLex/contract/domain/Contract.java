package com.aquaLex.contract.domain;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
@Entity
@Table(name = "contracts")
public class Contract {
   @Id
   @GeneratedValue(strategy = GenerationType.UUID)
   private UUID id;
   @Column(name ="exporter_name",nullable = false)
   private String exporterName;
   @Column (name ="importer_name",nullable = false)
   private String importerName;
   @Column (name ="product_name",nullable = false)
    private String productName;
    @Column (nullable = false, precision = 15, scale =3)
    private BigDecimal quantity;
    @Column (nullable = false, length = 50)
    private String unit;
    @Column (nullable = false, length = 10)
    private String currency;
    @Column(name = "unit_price",nullable = false, precision = 15, scale = 2)
    private BigDecimal unitPrice;
    @Column(nullable = false, length = 20)
    private String incoterm;
    @Column (name = "departure_port",nullable = false)
    private String departurePort;
       @Column(name = "arrival_port", nullable = false)
    private String arrivalPort;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private ContractStatus status;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    protected Contract() {
    }

    public Contract(
            String exporterName,
            String importerName,
            String productName,
            BigDecimal quantity,
            String unit,
            String currency,
            BigDecimal unitPrice,
            String incoterm,
            String departurePort,
            String arrivalPort
    ) {
        this.exporterName = exporterName;
        this.importerName = importerName;
        this.productName = productName;
        this.quantity = quantity;
        this.unit = unit;
        this.currency = currency;
        this.unitPrice = unitPrice;
        this.incoterm = incoterm;
        this.departurePort = departurePort;
        this.arrivalPort = arrivalPort;
        this.status = ContractStatus.DRAFT;
        this.createdAt = Instant.now();
        this.updatedAt = this.createdAt;
    }

    public UUID getId() {
        return id;
    }

    public String getExporterName() {
        return exporterName;
    }

    public String getImporterName() {
        return importerName;
    }

    public String getProductName() {
        return productName;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public String getUnit() {
        return unit;
    }

    public String getCurrency() {
        return currency;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public String getIncoterm() {
        return incoterm;
    }

    public String getDeparturePort() {
        return departurePort;
    }

    public String getArrivalPort() {
        return arrivalPort;
    }

    public ContractStatus getStatus() {
        return status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
    public void update(
            String exporterName,
            String importerName,
            String productName,
            BigDecimal quantity,
            String unit,
            String currency,
            BigDecimal unitPrice,
            String incoterm,
            String departurePort,
            String arrivalPort
    ) {
        if (exporterName != null) {
        this.exporterName = exporterName;
    }

    if (importerName != null) {
        this.importerName = importerName;
    }

    if (productName != null) {
        this.productName = productName;
    }

    if (quantity != null) {
        this.quantity = quantity;
    }

    if (unit != null) {
        this.unit = unit;
    }

    if (currency != null) {
        this.currency = currency;
    }

    if (unitPrice != null) {
        this.unitPrice = unitPrice;
    }

    if (incoterm != null) {
        this.incoterm = incoterm;
    }

    if (departurePort != null) {
        this.departurePort = departurePort;
    }

    if (arrivalPort != null) {
        this.arrivalPort = arrivalPort;
    }
        this.updatedAt = Instant.now();
    }
}
