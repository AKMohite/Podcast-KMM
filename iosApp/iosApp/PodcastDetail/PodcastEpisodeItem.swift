//
//  PodcastEpisodeItem.swift
//  iosApp
//
//  Created by Ashish Mohite + on 11/06/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import shared

struct PodcastEpisodeItem: View {
    let episode: PodcastEpisode
    var body: some View {
        if #available(iOS 16.0, *) {
            ZStack {
                VStack(alignment: .leading, spacing: 12) {
                    Text(episode.readableTime())
                        .font(.caption)
                    Text(episode.title)
                        .font(.title3)
                        .fontWeight(.bold)
                        .lineLimit(2)
                    
                    Text(episode.readableDuration())
                        .font(.body)
                }
                .padding(12)
            }
            .frame(maxWidth: .infinity, alignment: .leading)
            .cornerRadius(20) /// make the background rounded
            .overlay( /// apply a rounded border
                RoundedRectangle(cornerRadius: 16)
                    .stroke(.blue.opacity(0.4), lineWidth: 2)
            )
            
            
        } else {
            // Fallback on earlier versions
        }
        
    }
}

struct PodcastEpisodeItem_Previews: PreviewProvider {
    static var previews: some View {
        PodcastEpisodeItem(episode: sampleEpisodes[0])
    }
}
