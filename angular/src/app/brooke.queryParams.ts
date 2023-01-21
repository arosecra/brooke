
export declare interface BrookeLocation {
    collection: string;
    category?: string;
    series?: string;
    item?: string;
    leftPage?: string;
    rightPage?: string;
}

class BrookeQueryParamsImpl {

    fromQueryParamsToLocation(queryParams: any): BrookeLocation {
        return {
            collection: queryParams['collection'],
            category: queryParams['category'],
            series: queryParams['series'],
            item: queryParams['item'],
            leftPage: queryParams['leftPage'],
            rightPage: queryParams['rightPage'],
        } as BrookeLocation;
    }

    fromParametersToLocation() {

    }

    fromLocationToQueryParams(location: BrookeLocation) {
        return {

        }
    }

}

export const BrookeQueryParams = new BrookeQueryParamsImpl;